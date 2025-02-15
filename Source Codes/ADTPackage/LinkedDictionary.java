package ADTPackage;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class that implements the ADT dictionary by using a chain of linked nodes.
 * The dictionary is unsorted, and it can have duplicate search keys.
 * Search keys and associated values are not null.
 *
 * @author Frank M. Carrano
 * @author Timothy M. Henry
 * @version 5.0
 */
public class LinkedDictionary<K, V> implements DictionaryInterface<K, V> {
    private Node firstNode; // Reference to the first node of the chain
    private int numberOfEntries;

    public LinkedDictionary() {
        initializeDataFields();
    } // end default constructor

    public V add(K key, V value) {
        V result = null;
        if ((key == null) || (value == null)) {
            throw new IllegalArgumentException("Cannot add null to a dictionary.");
        } else {
            Node currentNode = firstNode;
            Node nodeBefore = null;

            // Search for the node with the given key
            while ((currentNode != null) && !key.equals(currentNode.getKey())) {
                nodeBefore = currentNode;
                currentNode = currentNode.getNextNode();
            }

            if (currentNode != null) {
              /*  // Key already exists, overwrite the value
                result = currentNode.getValue();
                currentNode.setValue(value);*/
            } else {
                // Key not found, add a new node at the beginning
                Node newNode = new Node(key, value);
                newNode.setNextNode(firstNode);
                firstNode = newNode;
                numberOfEntries++;
            }
        }
        return result;
    } // end add

    public V remove(K key) {
        V result = null;
        Node currentNode = firstNode;
        Node nodeBefore = null;

        // Search for the node with the given key
        while ((currentNode != null) && !key.equals(currentNode.getKey())) {
            nodeBefore = currentNode;
            currentNode = currentNode.getNextNode();
        }

        if (currentNode != null) {
            // Key found, remove the node
            Node nodeAfter = currentNode.getNextNode();

            if (nodeBefore == null) {
                firstNode = nodeAfter;
            } else {
                nodeBefore.setNextNode(nodeAfter);
            }

            result = currentNode.getValue();
            numberOfEntries--;
        }
        return result;
    } // end remove

    public V getValue(K key) {
        V result = null;
        Node currentNode = firstNode;

        // Search for the node with the given key
        while ((currentNode != null) && !key.equals(currentNode.getKey())) {
            currentNode = currentNode.getNextNode();
        }

        if (currentNode != null) {
            // Key found, get the value
            result = currentNode.getValue();
        }
        return result;
    } // end getValue

    public boolean contains(K key) {
        return getValue(key) != null;
    } // end contains

    public boolean isEmpty() {
        return numberOfEntries == 0;
    } // end isEmpty

    public int getSize() {
        return numberOfEntries;
    } // end getSize

    public final void clear() {
        initializeDataFields();
    } // end clear

    public Iterator<K> getKeyIterator() {
        return new KeyIterator();
    } // end getKeyIterator

    public Iterator<V> getValueIterator() {
        return new ValueIterator();
    } // end getValueIterator

    private void initializeDataFields() {
        firstNode = null;
        numberOfEntries = 0;
    } // end initializeDataFields

    private class KeyIterator implements Iterator<K> {
        private Node nextNode;

        private KeyIterator() {
            nextNode = firstNode;
        } // end default constructor

        public boolean hasNext() {
            return nextNode != null;
        } // end hasNext

        public K next() {
            K result;

            if (hasNext()) {
                result = nextNode.getKey();
                nextNode = nextNode.getNextNode();
            } else {
                throw new NoSuchElementException();
            }
            return result;
        } // end next

        public void remove() {
            throw new UnsupportedOperationException();
        } // end remove
    } // end KeyIterator

    private class ValueIterator implements Iterator<V> {
        private Node nextNode;

        private ValueIterator() {
            nextNode = firstNode;
        } // end default constructor

        public boolean hasNext() {
            return nextNode != null;
        } // end hasNext

        public V next() {
            V result;

            if (hasNext()) {
                result = nextNode.getValue();
                nextNode = nextNode.getNextNode();
            } else {
                throw new NoSuchElementException();
            }
            return result;
        } // end next

        public void remove() {
            throw new UnsupportedOperationException();
        } // end remove
    } // end ValueIterator

    private class Node {
        private K key;
        private V value;
        private Node next;

        private Node(K searchKey, V dataValue) {
            key = searchKey;
            value = dataValue;
            next = null;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V newValue) {
            value = newValue;
        }

        private Node getNextNode() {
            return next;
        }

        private void setNextNode(Node nextNode) {
            next = nextNode;
        }
    } // end Node
} // end UnsortedLinkedDictionary
