package me.corruptionhades.customcosmetics.md5;
import java.util.ArrayList;
import java.util.List;

class MD5File {
    private String version;
    private int numJoints, numMeshes;
    private List<Joint> joints = new ArrayList<>();
    private List<Mesh> meshes = new ArrayList<>();

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setNumJoints(int numJoints) {
        this.numJoints = numJoints;
    }

    public int getNumJoints() {
        return numJoints;
    }

    public void setNumMeshes(int numMeshes) {
        this.numMeshes = numMeshes;
    }

    public int getNumMeshes() {
        return numMeshes;
    }

    public List<Joint> getJoints() {
        return joints;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public void addJoint(Joint joint) {
        this.joints.add(joint);
    }

    public void addMesh(Mesh mesh) {
        this.meshes.add(mesh);
    }
}

class Joint {
    private String name;
    private int parentIndex;
    private float[] position = new float[3];
    private float[] orientation = new float[3];

    public Joint(String name, int parentIndex, float[] position, float[] orientation) {
        this.name = name;
        this.parentIndex = parentIndex;
        this.position = position;
        this.orientation = orientation;
    }

    //region Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getOrientation() {
        return orientation;
    }

    public void setOrientation(float[] orientation) {
        this.orientation = orientation;
    }


    //endregion
}

class Mesh {
    private String shader;
    private int numVerts;
    private int numTris;
    private int numWeights;
    private List<Vert> verts = new ArrayList<>();
    private List<Tri> tris = new ArrayList<>();
    private List<Weight> weights = new ArrayList<>();

    //region Getters and setters

    public String getShader() {
        return shader;
    }

    public void setShader(String shader) {
        this.shader = shader;
    }

    public int getNumVerts() {
        return numVerts;
    }

    public void setNumVerts(int numVerts) {
        this.numVerts = numVerts;
    }

    public int getNumTris() {
        return numTris;
    }

    public void setNumTris(int numTris) {
        this.numTris = numTris;
    }

    public int getNumWeights() {
        return numWeights;
    }

    public void setNumWeights(int numWeights) {
        this.numWeights = numWeights;
    }

    public List<Vert> getVerts() {
        return verts;
    }

    public void setVerts(List<Vert> verts) {
        this.verts = verts;
    }

    public List<Tri> getTris() {
        return tris;
    }

    public void setTris(List<Tri> tris) {
        this.tris = tris;
    }

    public List<Weight> getWeights() {
        return weights;
    }

    public void setWeights(List<Weight> weights) {
        this.weights = weights;
    }

    //endregion

    public void addVert(Vert vert) {
        this.verts.add(vert);
    }

    public void addTri(Tri tri) {
        this.tris.add(tri);
    }

    public void addWeight(Weight weight) {
        this.weights.add(weight);
    }
}

class Vert {
    private int index;
    private float[] texCoord = new float[2];
    private int startWeight;
    private int countWeight;

    //region Getters and setters

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float[] getTexCoord() {
        return texCoord;
    }

    public void setTexCoord(float[] texCoord) {
        this.texCoord = texCoord;
    }

    public int getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(int startWeight) {
        this.startWeight = startWeight;
    }

    public int getCountWeight() {
        return countWeight;
    }

    public void setCountWeight(int countWeight) {
        this.countWeight = countWeight;
    }


    //endregion
}

class Tri {
    private int index;
    private int[] vertIndices = new int[3];

    //region Getters and setters

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int[] getVertIndices() {
        return vertIndices;
    }

    public void setVertIndices(int[] vertIndices) {
        this.vertIndices = vertIndices;
    }

    //endregion
}

class Weight {
    private int index;
    private int joint;
    private float bias;
    private float[] position = new float[3];

    //region Getters and setters

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getJoint() {
        return joint;
    }

    public void setJoint(int joint) {
        this.joint = joint;
    }

    public float getBias() {
        return bias;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    //endregion
}
