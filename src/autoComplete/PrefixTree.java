package autoComplete;

import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        TreeNode newNode;
        TreeNode oldNode = root;
        Character chr;
        if(!contains(word)) {
            for(int i = 0; i < word.length(); i++) {
                chr = word.charAt(i);
                newNode = oldNode.getChildNode(chr);

                if(newNode == null) {
                    newNode = new TreeNode();
                    newNode.setLetter(chr);
                    oldNode.addChild(chr, newNode);
                }

                if(i == word.length() - 1) {
                    newNode.setIsWord(true);
                }

                oldNode = newNode;
            }
            size++;
        }
    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        char currentChar;
        TreeNode currentNode = root;
        TreeNode childNode;
        for(int i = 0; i < word.length(); i ++) {
            currentChar = word.charAt(i);
            childNode = currentNode.getChildNode(currentChar);
            if(childNode == null){
                return false;
            }
            currentNode = childNode;
        }
        return currentNode.getIsWord();
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> prefixList = new ArrayList<>();
        char currentChar;
        TreeNode currentNode = root;


        for(int i = 0; i < prefix.length(); i++) {
            currentChar = prefix.charAt(i);
            currentNode = currentNode.getChildNode(currentChar);
            if(currentNode == null) {
                return prefixList;
            }
        }

        preOrderTraverse(currentNode, prefix, prefixList);

        return prefixList;
    }

    /**
     * Perform a preorder traversal.
     *
     * @param node The local root
     * @param prefix The prefix
     * @param strList The list of strings to add the words to
     */
    private void preOrderTraverse(TreeNode node, String prefix, ArrayList<String> strList) {
        Map<Character, TreeNode> children = node.getChildren();
        if(node.isWord) {
            strList.add(prefix);
        }

        for (Map.Entry<Character, TreeNode> entry : children.entrySet()) {
            TreeNode newNode = entry.getValue();
            String newPrefix = prefix + entry.getKey();
            preOrderTraverse(newNode, newPrefix, strList);
        }
    }

    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }
    
}
