package com.example.textfinder;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Node<E> {
    E value;
    Node<E> next;

    public Node(E value) {
        this.value = value;
        this.next = null;
    }
}
public class LinkedListLibrary<E> implements Iterable<E> {

    private Node<E> head;
    private int size;

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private Node<E> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E value = current.value;
            current = current.next;
            return value;
        }
    }

    public LinkedListLibrary() {
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

    public boolean contains(E element) {
        Node<E> current = this.head;
        while (current != null) {
            if (current.value.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean add(E element) {
        Node<E> newNode = new Node<>(element);
        if (this.head == null) {
            this.head = newNode;
        } else {
            Node<E> current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        this.size++;
        return true;
    }

    public boolean remove(E element) {
        if (this.head == null) {
            return false;
        }
        if (this.head.value.equals(element)) {
            this.head = this.head.next;
            this.size--;
            return true;
        }
        Node<E> current = this.head;
        while (current.next != null) {
            if (current.next.value.equals(element)) {
                current.next = current.next.next;
                this.size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public E get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        Node<E> current = this.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public void set(int index, E element) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        Node<E> current = this.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.value = element;
    }

    public E get(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        Node<E> current = this.head;
        while (current != null) {
            if (current.value instanceof File && ((File) current.value).getName().equals(name)) {
                return current.value;
            }
            current = current.next;
        }
        throw new NoSuchElementException("El elemento con el nombre '" + name + "' no se encontró en la lista");
    }

    public void printList() {
        Node<E> current = head;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }
}
