package me.corruptionhades.customcosmetics;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.*;

public class JarAnalyzer extends ClassVisitor {

    private final String fileName;

    private JarAnalyzer(String fileName) {
        super(ASM9);
        this.fileName = fileName;
    }

    public static void main(String[] args) throws IOException {
        scanJar(
                new File("/home/mitarbeiter/IdeaProjects/CustomCosmetics/.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-merged-4c1b918aa2/1.21.3-net.fabricmc.yarn.1_21_3.1.21.3+build.2-v2/minecraft-merged-4c1b918aa2-1.21.3-net.fabricmc.yarn.1_21_3.1.21.3+build.2-v2.jar")
        );

    }

    public static void scanJar(File jarFile) throws IOException {
        var jf = new JarFile(jarFile);

        jf.stream().filter(je -> getFileExtension(je.getName()).equals(".class"))
                .forEach(je -> processEntry(jf, je));
    }

    private static String getFileExtension(String name) {
        int idx = name.lastIndexOf(".");
        if (idx == -1)
            return "";
        return name.substring(idx);
    }

    private static void processEntry(JarFile jf, JarEntry je) {
        try {
            byte[] bytes = jf.getInputStream(je).readAllBytes();
            JarAnalyzer ms = new JarAnalyzer(jf.getName() + "!" + je.getName());
            ClassReader cr = new ClassReader(bytes);
            cr.accept(ms, 0);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        return new MethodScannerVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), fileName, name + descriptor);
    }

    private static class MethodScannerVisitor extends MethodVisitor {

        private final String fileName;
        private final String methodName;

        private MethodScannerVisitor(MethodVisitor parent, String fileName, String methodName) {
            super(ASM7, parent);
            this.fileName = fileName;
            this.methodName = methodName;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            if (name.equals("tickHandSwing")) {
                System.out.println("reference to vertextBuffer#draw from " + fileName + " in " + methodName);
            }
        }
    }

    private static class FieldReferenceMethodVisitor extends MethodVisitor {

        private final String fieldName;

        public FieldReferenceMethodVisitor(MethodVisitor methodVisitor, String fieldName) {
            super(ASM9, methodVisitor);
            this.fieldName = fieldName;
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
            if (name.equals(fieldName)) {
                System.out.println("Reference to field " + fieldName + " found in method.");
            }
        }
    }
}
