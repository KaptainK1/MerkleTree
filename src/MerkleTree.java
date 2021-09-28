import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class MerkleTree {

    private MerkleNode merkleRoot;
    private int size;
    private int levels;
    public String hashingAlgorithm = "SHA-256";
    private List<MerkleNode> leafs = new ArrayList<>();

    public MerkleTree(ArrayList<Integer> unspentTransactions){

        if (unspentTransactions.size() % 2 == 0){
            this.size = unspentTransactions.size();
        } else {
            //we have an uneven tree, need to add 1 dummy node
            this.size = unspentTransactions.size() +1;
        }

        //calculate the number of levels needed for our tree
        this.levels = (int) Math.ceil((Math.log10(size) / Math.log10(2)));

        //create all the leaf nodes
        for (int i = 0; i < unspentTransactions.size(); i++) {
            //create a byte buffer and allocate 32 bytes for the hash output of 256
            ByteBuffer b = ByteBuffer.allocate(32);

            //we need to covert the data into a byte array by using the byte buffer
            b.putInt(unspentTransactions.get(i));
            byte[] data = b.array();

            //add the leaf node
            leafs.add(new MerkleNode(SHAUtils.digest(data,hashingAlgorithm),null, null));

        }

        int numberOfDuplicateNodes = (int)(Math.pow(2,this.levels) - size);

        for (int i = 0; i < numberOfDuplicateNodes; i++) {
            leafs.add(new MerkleNode(leafs.get(i+size).getData(),null,null));
        }

        //int numberOfNodesNeeded = (int) (Math.pow(2,unspentTransations.size()));


    }

    public void buildMerkleTree(ArrayList<Integer> unspentTransactions){

        ArrayList<MerkleNode> parents = new ArrayList<>();
        parents = createDirectParents(parents);


        for (int i = 3; i < this.levels +1; i++) {


            for (int j = 0; j < (leafs.size() / i); j+=2) {

                byte[] combinedHash = SHAUtils.concatenateHash(parents.get(j+i).getData(), parents.get(j+i+1).getData(), hashingAlgorithm);
                MerkleNode node = new MerkleNode(combinedHash,parents.get(j+i), parents.get(j+i+1));
                parents.add(node);

            }

        }

    }

    private ArrayList<MerkleNode> createDirectParents(ArrayList<MerkleNode> parents){

        for (int i = 0; i < leafs.size() / 2; i+=2) {

            byte[] combinedHash = SHAUtils.concatenateHash(leafs.get(i).getData(), leafs.get(i+1).getData(), hashingAlgorithm);
            MerkleNode directParent = new MerkleNode(combinedHash, leafs.get(i), leafs.get(i+1));
            parents.add(directParent);
        }

        return parents;

    }


//    private void insertMerkleNode(byte data[], MerkleNode left, MerkleNode right){
//        MerkleNode node = new MerkleNode();
//
//        if (left == null && right == null){
//            node.setData(SHAUtils.digest(data, hashingAlgorithm));
//        } else if(left == null){
//            byte prevHashes[] = new byte[left.getData().length + right.getData().length];
//            System.arraycopy(left.getData(), 0, prevHashes,0, left.getData().length);
//            System.arraycopy(right.getData(),0,prevHashes, left.getData().length +1,right.getData().length);
//
//            node.setData(SHAUtils.digest(prevHashes, hashingAlgorithm));
//        }
//
//        node.setLeft(left);
//        node.setRight(right);
//
//    }



}
