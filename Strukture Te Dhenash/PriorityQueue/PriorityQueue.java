package PriorityQueue;

import java.util.AbstractCollection;

import java.util.Collection;

import java.util.Comparator;

import java.util.Iterator;

import java.util.NoSuchElementException;

public class PriorityQueue<AnyType> extends AbstractCollection<AnyType> {

    private static final int DEFAULT_CAPACITY = 100;

    private int currentSize;

    private AnyType[] array;

    private Comparator<? super AnyType> cmp;

    @SuppressWarnings("unchecked")

    public PriorityQueue() {

        currentSize = 0;

        cmp = null;

        array = (AnyType[]) new Object[DEFAULT_CAPACITY + 1];

    }

    public PriorityQueue(Comparator<? super AnyType> c) {

        currentSize = 0;

        cmp = c;

        array = (AnyType[]) new Object[DEFAULT_CAPACITY + 1];

    }

    public PriorityQueue(Collection<? extends AnyType> coll) {

        cmp = null;

        currentSize = coll.size();

        array = (AnyType[]) new Object[(currentSize + 2) * 11 / 10];

        int i = 1;

        for (AnyType item : coll)

            array[i++] = item;

        buildHeap();

    }

    public int size() {

        return currentSize;

    }

    public void clear() {

        currentSize = 0;

    }

    public AnyType element() {

        if (isEmpty())

            throw new NoSuchElementException();

        return array[1];

    }

    public boolean add(AnyType x) {

        if (currentSize + 1 == array.length)

            doubleArray();

// Percolate up

        int hole = ++currentSize;

        array[0] = x;

        for (; compare(x, array[hole / 2]) < 0; hole /= 2)

            array[hole] = array[hole / 2];

        array[hole] = x;

        return true;

    }

    public AnyType remove() {

        AnyType minItem = element();

        array[1] = array[currentSize--];

        percolateDown(1);

        return minItem;

    }

    private void percolateDown(int hole) {

        int child;

        AnyType tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {

            child = hole * 2;

            if (child != currentSize && compare(array[child + 1], array[child]) < 0)

                child++;

            if (compare(array[child], tmp) < 0)

                array[hole] = array[child];

            else

                break;

        }

        array[hole] = tmp;

    }

    private void buildHeap() {

        for (int i = currentSize / 2; i > 0; i--)

            percolateDown(i);

    }

    private void doubleArray() {

        AnyType[] newArray;

        newArray = (AnyType[]) new Object[array.length * 2];

        for (int i = 0; i < array.length; i++) {

            newArray[i] = array[i];

        }

        array = newArray;

    }

    private int compare(AnyType lhs, AnyType rhs) {

        if (cmp == null)

            return ((Comparable) lhs).compareTo(rhs);

        else

            return cmp.compare(lhs, rhs);

    }

    public void zvogeloPrioritet(int index, int sasia) {

        array[index] = (AnyType) (Object) ((int) array[index] - sasia);

        percolateDown(index);

    }

    public void fshi(int index) {

        array[index] = array[currentSize--];

        buildHeap();

    }

    public void print() {

        for (int i = 1; i <= size(); i++)

            System.out.print(" " + array[i]);

    }

    @Override

    public Iterator<AnyType> iterator() {

// TODO Auto-generated method stub

        return null;

    }

}
