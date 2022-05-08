/**
 * A4DasPritha
 *
 * COMP 2140 SECTION A02
 * INSTRUCTOR    Cameron (A02)
 * ASSIGNMENT    Assignment 4
 * @author       Pritha Das, 7924180
 * @version      November 23, 2021
 *
 * PURPOSE: Play 20 Questions using Decision Trees and save knowledge to a file
 */

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.util.Scanner;
import java.io.*;

public class A4DasPritha{
    /**
     * PURPOSE : Main method to test all other methods and classes
     */
    public static void main(String[] args){
        A4DasPritha obj = new A4DasPritha();
        obj.playGame();
        JOptionPane.showMessageDialog(null,"20 Questions ends.");
    } // end main method

    /**
     * PURPOSE : Helper method to main that creates an object of the Questioner class and calls all other methods appropriately.
     *           It also takes care of the fundamental structure of the game.
     */
    private void playGame(){
        Questioner question = new Questioner();
        int answer = JOptionPane.showConfirmDialog(null, "Let's play 20 Questions", "20 Questions", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        JOptionPane.showMessageDialog(null,"Think about a living creature!");

        while (answer == JOptionPane.YES_OPTION) {
            question.playRound();
            answer = JOptionPane.showConfirmDialog(null, "I get better everytime I play. Another round?", "20 Questions",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(answer == JOptionPane.NO_OPTION) {
                int override = JOptionPane.showConfirmDialog(null, "Save knowledge in file?", "Progress...", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(override == JOptionPane.YES_OPTION)
                    question.writeTree();
            } // end nested if
        } // end while
    } // end method playGame

    /**
     * PURPOSE : Creates and modifies the entire decision tree
     */
    private class Questioner{

        /**
         *  PURPOSE : Creates a Decision tree node
         */
        private class DTNode{
            public String item;
            public DTNode left;
            public DTNode right;

            /**
             * PURPOSE : Creates a leaf node
             * @param item : String variable to store the question
             */
            public DTNode(String item){
                this.item = item;
            } // end DTNode (leaf) constructor

            /**
             * PURPOSE : Creates an internal node
             * @param item : String variable to store the question
             * @param left : Node that links to the left child
             * @param right : Node that links to the right child
             */
            public DTNode(String item, DTNode left, DTNode right){
                this.item = item;
                this.left = left;
                this.right = right;
            } // end DTNode (internal) constructor

            /**
             * PURPOSE : Checks if a node is a leaf node
             * @param newNode : Accepts a node
             * 
             * @return boolean : returns true if leaf,false if not.
             */
            public boolean isLeaf(DTNode newNode){
                return (newNode.left == null && newNode.right == null);
            } // end isLeaf method
        } // end DTNode class

        public DTNode root;

        /**
         * PURPOSE : Sets up an initial hardcoded tree
         */
        public Questioner(){
            root = new DTNode ("");
            root.left = new DTNode ("");
            root.left.left = new DTNode ("human");
            root.left.right = new DTNode ("shark");
            root.right = new DTNode ("");
            root.right.left = new DTNode ("carrot");
            root.right.right = new DTNode ("diamond");
            root.left = new DTNode ("Is it a mammal?",root.left.left, root.left.right);
            root.right = new DTNode ("Is it a plant?",root.right.left, root.right.right);
            root = new DTNode ("Is it an animal?", root.left, root.right);
        } // end Questioner constructor

        /**
         * PURPOSE : Initiates the game. Modifies the tree if the computer cannot guess.
         * @return String : returns if the computer correctly or incorrectly.
         */
        public String playRound(){
            DTNode curr = root;
            DTNode prev = null;
            DTNode newNode = null;
            String answer = "", newThing="", question="";
            int currAnswer=0;

            while(!curr.isLeaf(curr)){
                prev = curr;
                currAnswer = JOptionPane.showConfirmDialog(null, curr.item,"20 Questions", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(currAnswer == JOptionPane.YES_OPTION){
                    curr = curr.left;
                }
                else if(currAnswer == JOptionPane.NO_OPTION){
                    curr = curr.right;
                } // end if-else
            } // end while

            currAnswer = JOptionPane.showConfirmDialog(null, "Are you thinking of a/an " + curr.item +" ?",
                "20 Questions", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // Checks if the answer guessed is correct or not. If it is not correct then it asks 
            // for the user's guess and modifies the tree accordingly.
            if(currAnswer == JOptionPane.YES_OPTION)
                answer = "I guessed correctly!";
            else if(currAnswer == JOptionPane.NO_OPTION){
                answer = "I guessed wrong.";
                newThing = JOptionPane.showInputDialog("What were you thinking of?");
                question = JOptionPane.showInputDialog("Please give me a yes/no question to distinguish between a/an "+
                    curr.item + " and " + newThing);
                if (prev.left == curr)
                    prev.left = new DTNode(question, new DTNode(newThing), new DTNode(curr.item));
                else
                    prev.right = new DTNode(question, new DTNode(newThing), new DTNode(curr.item));
            } // end if-else
            return answer;
        } // end playRound method

        /**
         * PURPOSE : Writes the hardcoded tree into a file.
         */
        public void writeTree(){
            JFileChooser j = new JFileChooser();
            j.showSaveDialog(null);
            File file = j.getSelectedFile();

            try{
                PrintWriter outFile = new PrintWriter (new FileWriter (file));
                preorderTraversal(outFile,root);
                outFile.close();
            }
            catch(Exception e){
                System.out.println("Sorry, something went wrong");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } // end try-catch 
        } // end method writeTree

        /**
         * PURPOSE : Recursive helper method for writeTree. It traverses the tree in a preorder way.
         * @param outFile : Accepts a file where the tree is to be written
         * @param curr : Accepts a node to be se up as a part of the tree
         */
        void preorderTraversal(PrintWriter outFile, DTNode curr) {
            String lineStart="< ", lineEnd=">";
            String line="";
                
            line+= lineStart+ curr.item ;
            
            outFile.println(line);
            // left tree traversal
            if(curr.left!=null)
                preorderTraversal(outFile,curr.left);
                
            // right tree traversal
            if(curr.right!=null)
                preorderTraversal(outFile,curr.right);
                
            outFile.println(lineEnd);
        } // end method preorderTraversal 

        /**
         * PURPOSE : Reads a tree from a file. 
         */
        public void readTree(){
            JFileChooser j = new JFileChooser();
            j.showOpenDialog(null);
            File file = j.getSelectedFile();

            try{
                FileReader fr = new FileReader (file);
                Scanner scan = new Scanner(fr);
                root = helperTree(scan);
                fr.close();
            }
            catch(Exception e){
                System.out.println("Sorry, something went wrong");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } // end try-catch 
        } // end method readTree

        /**
         * PURPOSE : Recursive helper method for readTree. 
         * @param scan : Scanner argument
         * 
         * @return DTNode : Returns a node
         */
        private DTNode helperTree(Scanner scan){
            String line = "";
            DTNode curr = root;

            if(scan.hasNext("<")){
                line = scan.nextLine();
                if(scan.hasNext("<")){
                    // read left subtree
                    curr.left = helperTree (scan);
                    // read right subtree
                    curr.right = helperTree (scan);
                }
                // forms the tree
                curr = new DTNode (line, curr.left, curr.right);
            } // end nested if
            scan.nextLine();
            return curr;
        } // end method helperTree
    } // end class Questioner
} // end A4DasPritha
