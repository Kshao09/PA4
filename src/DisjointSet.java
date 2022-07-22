import java.util.*;
public class DisjointSet<T> {
    private int[] parent;
    private T[] elements;
    private HashMap<T, Integer> index;
    public DisjointSet(T[] elements) {
        index = new HashMap<T, Integer>();
        this.elements = elements;
        int i = 0;
        for(T element: elements)
            index.put(element, i++);
        parent = new int[elements.length];
        for(i = 0; i < parent.length;i++)
            parent[i] = -1;
    }
    public T find(T t) {
        int i = index.get(t);
        while(parent[i] >= 0)
            i = parent[i];
        return elements[i];
    }
    public void union(T u, T v) {
        int i = index.get(find(u));
        int j = index.get(find(v));
        if(parent[i] < parent[j]){//union by size
            parent[i] += parent[j];//update the size
            parent[j] = i;//add j to i
        }
        else{//union by size
            parent[j] += parent[i];//update the size
            parent[i] = j;//add j to i
        }
    }
}
