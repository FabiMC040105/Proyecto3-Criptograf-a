package com.example.textfinder;

import java.util.NoSuchElementException;

class Nodo {
    String document;
    int WordNumber;
    Nodo next;

    public Nodo(int value, String doc) {
        this.document = doc;
        this.WordNumber = value;
        this.next = null;
    }
}

public class LinkedListOcurrences {
    private Nodo head;
    private int size;

    public LinkedListOcurrences() {
        this.head = null;
        this.size = 0;
    }

    public void clear() {
        this.head = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public boolean contains(int element) {
        Nodo current = this.head;
        while (current != null) {
            if (current.WordNumber == element) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean add(int element, String doc) {
        Nodo newNode = new Nodo(element, doc);
        if (this.head == null) {
            this.head = newNode;
        } else {
            Nodo current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        this.size++;
        return true;
    }

    public int remove(int element) {
        if (this.head == null) {
            throw new NoSuchElementException();
        }
        if (this.head.WordNumber == element) {
            this.head = this.head.next;
            this.size--;
            return element;
        }
        Nodo current = this.head;
        while (current.next != null) {
            if (current.next.WordNumber == element) {
                current.next = current.next.next;
                this.size--;
                return element;
            }
            current = current.next;
        }
        throw new NoSuchElementException();
    }
    public String getListAsString() {
        StringBuilder listString = new StringBuilder();
        Nodo current = head;
        while (current != null) {
            listString.append("WordNumber: ").append(current.WordNumber).append(", Document: ").append(current.document).append("\s");
            current = current.next;
        }
        return listString.toString();
    }
    public void printList() {
        Nodo current = head;
        while (current != null) {
            System.out.println("Posición: " + current.WordNumber + ", Documento: " + current.document);
            current = current.next;
        }
    }

}
