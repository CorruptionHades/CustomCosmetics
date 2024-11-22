package me.corruptionhades.customcosmetics.md5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5FileReader {

    public MD5File parse(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        MD5File md5File = new MD5File();
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("MD5Version")) {
                md5File.setVersion(line.split(" ")[1]);
            } else if (line.startsWith("numJoints")) {
                md5File.setNumJoints(Integer.parseInt(line.split(" ")[1]));
            } else if (line.startsWith("numMeshes")) {
                md5File.setNumMeshes(Integer.parseInt(line.split(" ")[1]));
            } else if (line.startsWith("joints {")) {
                parseJoints(reader, md5File);
            } else if (line.startsWith("mesh {")) {
                parseMesh(reader, md5File);
            }
        }

        return md5File;
    }

    private void parseJoints(BufferedReader reader, MD5File md5File) throws Exception {
        String line;
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s+(-?\\d+)\\s+\\(\\s*([\\d.-]+)\\s+([\\d.-]+)\\s+([\\d.-]+)\\s*\\)\\s+\\(\\s*([\\d.-]+)\\s+([\\d.-]+)\\s+([\\d.-]+)\\s*\\)");
        while (!(line = reader.readLine()).equals("}")) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String name = matcher.group(1);
                int parentIndex = Integer.parseInt(matcher.group(2));
                float[] position = new float[]{Float.parseFloat(matcher.group(3)), Float.parseFloat(matcher.group(4)), Float.parseFloat(matcher.group(5))};
                float[] orientation = new float[]{Float.parseFloat(matcher.group(6)), Float.parseFloat(matcher.group(7)), Float.parseFloat(matcher.group(8))};
                md5File.addJoint(new Joint(name, parentIndex, position, orientation));
            }
        }
    }

    private void parseMesh(BufferedReader reader, MD5File md5File) throws Exception {
        String line;
        Mesh mesh = new Mesh();
        while (!(line = reader.readLine()).equals("}")) {
            if (line.startsWith("shader")) {
                mesh.setShader(line.split("\"")[1]);
            } else if (line.startsWith("numverts")) {
                mesh.setNumVerts(Integer.parseInt(line.split(" ")[1]));
            } else if (line.matches("\\s*vert\\s.*")) {

                // remove beginning and end whitespace
                line = line.trim();

                // Parse vert
                //   vert 544 ( 0.9374990463 0.0000000000 ) 544 1
                // The contents of the line after this string is the definition of the vertex. Following the "vert" string is an integer describing the index of the vertex. After that is the texture coordinates for this vertex (in parentheses). Then there is another integer, which is the index or ID of the "start weight" for this vertex. After the "start weight" is the number of weights this vertex uses. Since each vertex can be bound to one or more weights, the ID of the first weight is used, and the next "n-1" weights directly after this start weight will also be used to calculate the vertex's position (where "n" is the number of weights stored in this vertex).
                Vert vert = new Vert();
                String[] parts = line.split(" ");
                vert.setIndex(Integer.parseInt(parts[1]));
                vert.setTexCoord(new float[]{Float.parseFloat(parts[3].substring(1)), Float.parseFloat(parts[4].substring(0, parts[4].length() - 1))});
                vert.setStartWeight(Integer.parseInt(parts[6]));
                vert.setCountWeight(Integer.parseInt(parts[7]));
                mesh.addVert(vert);
            } else if (line.matches("\\s*numtris\\s.*")) {
                line = line.trim();
                mesh.setNumTris(Integer.parseInt(line.split(" ")[1]));
            } else if (line.matches("\\s*tri\\s.*")) {
                line = line.trim();
                // Parse tri
                //   tri 411 418 421 156
                Tri tri = new Tri();
                String[] parts = line.split(" ");
                tri.setIndex(Integer.parseInt(parts[1]));

                // Lines that start with "tri" are part of the index list making up this subset. The integer right after "tri" is the ID or index value of the current triangle, and the next three integers are the ID's or index values of the vertices that make up this triangle.
                tri.setVertIndices(new int[]{Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])});
                mesh.addTri(tri);
            } else if (line.matches("\\s*numweights\\s.*")) {
                line = line.trim();
                mesh.setNumWeights(Integer.parseInt(line.split(" ")[1]));
            } else if (line.matches("\\s*weight\\s.*")) {
                line = line.trim();
                // Parse weight
                // eg   weight 496 22 1.0000000000 ( 0.2308399081 -0.2281174809 -0.7724962234 )
                Weight weight = new Weight();
                String[] parts = line.split(" ");
                weight.setIndex(Integer.parseInt(parts[1]));
                weight.setJoint(Integer.parseInt(parts[2]));
                weight.setBias(Float.parseFloat(parts[3]));
                weight.setPosition(new float[]{Float.parseFloat(parts[5]), Float.parseFloat(parts[6]), Float.parseFloat(parts[7])});
                mesh.addWeight(weight);
            }
        }
        md5File.addMesh(mesh);
    }
}