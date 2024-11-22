package me.corruptionhades.customcosmetics;

import net.minecraft.client.render.item.ItemRenderer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.*;

public class JarAnalyzer extends ClassVisitor {

    private final String fileName;
    private final List<MethodSearch> searches;

    private JarAnalyzer(String fileName, List<MethodSearch> searches) {
        super(ASM9);
        this.fileName = fileName;
        this.searches = searches;
    }

    public static void main(String[] args) throws IOException {
        scanJar(
                new File("/home/mitarbeiter/IdeaProjects/CustomCosmetics/.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-merged-4c1b918aa2/1.21.3-net.fabricmc.yarn.1_21_3.1.21.3+build.2-v2/minecraft-merged-4c1b918aa2-1.21.3-net.fabricmc.yarn.1_21_3.1.21.3+build.2-v2.jar")
                , new ArrayList<MethodSearch>() {
                    {
                        add(MethodSearch.of(ItemRenderer.class, "renderItem"));
                    }
                }
        );

    }

    public static void scanJar(File jarFile, List<MethodSearch> searches) throws IOException {
        var jf = new JarFile(jarFile);

        jf.stream().filter(je -> getFileExtension(je.getName()).equals(".class"))
                .forEach(je -> processEntry(jf, je, searches));
    }

    private static String getFileExtension(String name) {
        int idx = name.lastIndexOf(".");
        if (idx == -1)
            return "";
        return name.substring(idx);
    }

    private static void processEntry(JarFile jf, JarEntry je, List<MethodSearch> searches) {
        try {
            byte[] bytes = jf.getInputStream(je).readAllBytes();
            JarAnalyzer ms = new JarAnalyzer(jf.getName() + "!" + je.getName(), searches);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(ms, 0);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        return new MethodScannerVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), fileName, name + descriptor, searches);
    }

    private static class MethodScannerVisitor extends MethodVisitor {

        private final String fileName;
        private final String methodName;
        private final List<MethodSearch> searches = new ArrayList<>();

        private MethodScannerVisitor(MethodVisitor parent, String fileName, String methodName, List<MethodSearch> searches) {
            super(ASM7, parent);
            this.fileName = fileName;
            this.methodName = methodName;
            this.searches.addAll(searches);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

            for (MethodSearch search : searches) {
                if (search.methodName.equals(name) && search.fileName.equals(owner)) {
                    System.out.println("Method " + name + " ref in " + fileName.split("/")[fileName.split("/").length - 1] + " in " + methodName);
                }
            }

            /*if (owner.equals("net/minecraft/client/render/item/ItemRenderer") && name.equals("renderItem")) {
                System.out.println("reference to vertextBuffer#draw from " + fileName.split("/")[fileName.split("/").length - 1] + " in " + methodName);
            }

            if(owner.equals("net.minecraft.client.render.entity.ItemEntityRenderer".replace(".", "/")) && name.equals("renderStack")) {
                System.out.println("ref to ItemEntityRenderer#render from " + fileName.split("/")[fileName.split("/").length - 1] + " in " + methodName);
            }*/
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

    private record MethodSearch(String methodName, String fileName) {
        public static MethodSearch of(Class<?> clazz, String methodName) {
            return new MethodSearch(methodName, clazz.getName().replace(".", "/"));
        }
    }
}
