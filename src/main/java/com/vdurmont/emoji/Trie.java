package com.vdurmont.emoji;

import java.util.HashMap;

/**
 * A simple trie implementation.
 *
 * @author Olumide Awofeso [olumideawofeso@gmail.com]
 */
public class Trie {
    private Node root;

    public Trie(){
        root = new Node();
    }

    public Node getRoot(){
        return root;
    }

    class Node{
        private HashMap<Integer, Node> nodes;
        private boolean isEnd = false;

        public Node(){
            this.nodes = new HashMap<Integer, Node>();
        }

        /**
         * This function signifies that a word ends at this node
         */
        public void setEnd(){
            isEnd = true;
        }

        public void add(int value){
            //Check if a node with this value already exists
            if(!nodeContains(value)){
                nodes.put(value, new Node());
            }
        }

        public boolean isEnd(){
            return isEnd;
        }

        public boolean nodeContains(int value){
            return nodes.containsKey(value);
        }

        public Node getNode(int value){
            return nodes.get(value);
        }
    }
}