import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MerkleTree {

    private MerkleNode merkleRoot;
    private int size;
    private int levels;
    public String hashingAlgorithm = "SHA-256";
    private List<MerkleNode> leafs = new ArrayList<>();
    private ArrayList<MerkleNode> parents = new ArrayList<>();

    public MerkleTree(ArrayList<String> unspentTransactions){

//        if (unspentTransactions.size() % 2 == 0){
//            this.size = unspentTransactions.size();
//        } else {
//            //we have an uneven tree, need to add 1 dummy node
//            this.size = unspentTransactions.size() +1;
//        }

        this.size = unspentTransactions.size();

        //calculate the number of levels needed for our tree
        this.levels = (int) Math.ceil((Math.log10(size) / Math.log10(2)));
        System.out.println("Levels: " + this.levels);

        //create all the leaf nodes
        for (int i = 0; i < unspentTransactions.size(); i++) {
            //create a byte buffer and allocate 32 bytes for the hash output of 256
            ByteBuffer b = ByteBuffer.allocate(32);

            //we need to covert the data into a byte array by using the byte buffer
            //b.put(unspentTransactions.get(i));
            b.put(unspentTransactions.get(i).getBytes());
            byte[] data = b.array();

            //add the leaf node
            leafs.add(new MerkleNode(SHAUtils.digest(data,hashingAlgorithm), unspentTransactions.get(i) ,null, null));

        }

        int numberOfDuplicateNodes = (int)(Math.pow(2,this.levels) - size);
        System.out.println("Number of duplicates: " + numberOfDuplicateNodes);

        //create some dummy nodes in order to create a full tree. we cant have a tree that does not equal
        //a power of two.
        for (int i = 0; i < numberOfDuplicateNodes; i++) {
            leafs.add(new MerkleNode(leafs.get(i+size -1).getData(), leafs.get(i+size -1).getUnHashedData() ,null,null));
        }

        buildMerkleTree(unspentTransactions);

    }

    //function to build the Merkle Tree from the bottom up
    public void buildMerkleTree(ArrayList<String> unspentTransactions){

        createDirectParents();

        System.out.println("Levels: " + this.levels);
        System.out.println("Size: " + this.size);

        //Since we build our direct parent nodes in another function,
        //our first two bottom levels are built, so we can start at the third level
        for (int i = this.levels - 2; i > this.levels; i--) {

            //Here we want to loop as many times as there are nodes at the ith level
            // which we can calculate with Math.pow(2,i-1)
            for (int j = 0; j <= (int) Math.pow(2,i); j+=2) {

                System.out.println(Math.pow(2,i-1));
                System.out.println("i is" + i);
                System.out.println("j is" + j);
                byte[] combinedHash = SHAUtils.concatenateHash(parents.get(j+i -1).getData(),
                                                                parents.get(j+i).getData(),
                                                                hashingAlgorithm);

                MerkleNode node = new MerkleNode(combinedHash,parents.get(j+i -1).getUnHashedData() + parents.get(j+i).getUnHashedData(),
                                                    parents.get(j+i -1),
                                                    parents.get(j+i ));

                parents.add(node);
                System.out.println(node);

            }

        }

        //set the merkle root to the last element in the parents array list
        byte[] combinedHash = SHAUtils.concatenateHash(parents.get(parents.size() -2).getData(),
                parents.get(parents.size() -1).getData(),
                hashingAlgorithm);

        this.merkleRoot = new MerkleNode(combinedHash, parents.get(parents.size() -2).getUnHashedData() + parents.get(parents.size() -1).getUnHashedData(), parents.get(parents.size() -2), parents.get(parents.size() -1));

    }

    public void printMerkleTree(){

        //loop through each of the leaf nodes to print them out
        //this is our data nodes, in the future these will be our
        //transactions. For now they are just integers

        printInOrder(this.merkleRoot);

    }

    public void printInOrder(MerkleNode node){
        if (node == null)
            return;

        printInOrder(node.getLeft());

        System.out.println(node.getUnHashedData());

        printInOrder(node.getRight());
    }

    private void createDirectParents(){

        for (int i = 0; i < leafs.size() ; i+=2) {

            byte[] combinedHash = SHAUtils.concatenateHash(leafs.get(i).getData(),
                                                            leafs.get(i+1).getData(),
                                                            hashingAlgorithm);
            MerkleNode directParent = new MerkleNode(combinedHash,
                                                        leafs.get(i).getUnHashedData() + leafs.get(i+1).getUnHashedData(),
                                                        leafs.get(i),
                                                        leafs.get(i+1));
            parents.add(directParent);
            System.out.println(directParent.getUnHashedData());
        }

    }

    public static void main(String[] args){
        String[] strings = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        ArrayList<String> test = new ArrayList<String>(
                Arrays.asList(strings)
                );


        System.out.println(test);

        MerkleTree tree = new MerkleTree(test);

        tree.printMerkleTree();

        System.out.println(tree.merkleRoot.getUnHashedData());
        System.out.println(tree.merkleRoot);

    }

}
