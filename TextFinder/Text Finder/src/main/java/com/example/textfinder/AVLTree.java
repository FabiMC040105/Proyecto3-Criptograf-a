package com.example.textfinder;

class AVLNode {
    String key;
    int height;
    AVLNode left, right;
    LinkedListOcurrences ocurrences = new LinkedListOcurrences();

    public AVLNode(String  key, int numberword, String doc) {
        this.key = key;
        this.height = 1;  // Un nodo nuevo tiene siempre altura 1
        this.left = null;
        this.right = null;
        ocurrences.add(numberword, doc);
    }
}

// Estructura básica de un Árbol AVL
public class AVLTree {
    private AVLNode root;

    // Constructor
    public AVLTree() {
        root = null;
    }

    // Método público para insertar un nuevo nodo
    public void insert(String key, int numberword, String doc) {
        root = insert(root, key, numberword, doc);
    }

    // Método recursivo privado para insertar un nuevo nodo
    private AVLNode insert(AVLNode node, String key, int numberword, String doc) {
        if (node == null) {
            return new AVLNode(key, numberword, doc);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = insert(node.left, key, numberword, doc);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insert(node.right, key, numberword, doc);
        } else {
            // Si la palabra ya existe en el árbol, solo se añade la ocurrencia
            node.ocurrences.add(numberword, doc);
            return node;
        }
        // Actualizar altura de este nodo ancestro
        node.height = 1 + Math.max(height(node.left), height(node.right));
        // Obtener el factor de equilibrio
        int balance = getBalance(node);
        // Si el nodo se desbalanceó, hay 4 casos
        // Caso izquierda-izquierda
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rotateRight(node);
        }
        // Caso derecha-derecha
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return rotateLeft(node);
        }
        // Caso izquierda-derecha
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Caso derecha-izquierda
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        // Devolver el nodo (cambiado o no)
        return node;
    }

    // Método para obtener la altura del árbol
    private int height(AVLNode N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // Método para obtener el factor de equilibrio de un nodo
    private int getBalance(AVLNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Rotación a la derecha
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Realizar rotación
        x.right = y;
        y.left = T2;

        // Actualizar alturas
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Retornar nueva raíz
        return x;
    }

    // Rotación a la izquierda
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Realizar rotación
        y.left = x;
        x.right = T2;

        // Actualizar alturas
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Retornar nueva raíz
        return y;
    }
    // Método público para realizar recorrido inOrder
    public void inOrder() {
        inOrderRecursive(root);
    }

    // Método privado recursivo para realizar recorrido inOrder
    private void inOrderRecursive(AVLNode node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.print(node.key + ": ");
            node.ocurrences.printList();
            System.out.println("\n");
            inOrderRecursive(node.right);
        }
    }
}