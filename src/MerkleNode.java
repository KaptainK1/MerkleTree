import java.lang.reflect.Array;
import java.util.Arrays;

public class MerkleNode {

    private byte data[];

    private MerkleNode left;
    private MerkleNode right;

    public MerkleNode(byte data[], MerkleNode left, MerkleNode right){
        //clone the data (ie data to be hashed)
        this.data = data.clone();
        this.left = left;
        this.right = right;
    }

    public MerkleNode(){
        this.data = null;
        this.left = null;
        this.right = null;
    }

//getters and setters
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data.clone();
    }

    public MerkleNode getLeft() {
        return left;
    }

    public void setLeft(MerkleNode left) {
        this.left = left;
    }

    public MerkleNode getRight() {
        return right;
    }

    public void setRight(MerkleNode right) {
        this.right = right;
    }


}
