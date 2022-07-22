/**
 * Kenny Shao
 * Prof. Kianoosh Boroojeni
 * This program finds the median of two equal sized arrays, along with the using a graph to verify if the islands are connected through bridges or not.
 * This program also uses topological sort to determine the course path of a cs student and which classes to take using Depth-First Search.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int[] list1 = {3,5,8,9};
        int[] list2 = {2,6,10,12};
        System.out.println(findMedian(list1,list2));
        System.out.println("--------------------------------------------");

        String[]names = new String[]{"Mario", "Maria", "Bob", "Ann"};
        HashMap<String, List<String>> bridges = new HashMap<>();
        bridges.put("Mario", new ArrayList<String>());
        bridges.get("Mario").add("Maria");
        bridges.put("Ann", new ArrayList<>());
        bridges.get("Ann").add("Bob");
        System.out.println(getIsland(names, bridges, "Mario", "Bob"));
        System.out.println(getIsland(names, bridges, "Ann", "Bob"));
        bridges.get("Mario").add("Bob");
        System.out.println(getIsland(names, bridges, "Mario", "Bob"));
        System.out.println(getIsland(names, bridges, "Maria", "Bob"));
        System.out.println(getIsland(names, bridges, "Maria", "Ann"));

        System.out.println("-------------------------------------------------");

        HashMap<Vertex, Set<Vertex>> graph = new HashMap<Vertex, Set<Vertex>>();
        HashSet<Vertex> vertexSet = new HashSet<Vertex>();
        Scanner s = new Scanner(new File("src\\prerequisites.txt"));
        s.nextLine();
        while(s.hasNextLine()){
            String line = s.nextLine();
            Vertex course = new Vertex(line.substring(0,line.indexOf('\t')));
            vertexSet.add(course);
            String[] prereqs = line.substring(line.indexOf('\t')).split(",");
            for(int i = 0; i < prereqs.length;i++){
                prereqs[i] = prereqs[i].replace("\"", "");
                prereqs[i] = prereqs[i].replace("\t", "");
            }

            for(String prereq: prereqs){
                Vertex v = new Vertex(prereq);
                vertexSet.add(v);
                if(!graph.containsKey(v))
                    graph.put(v, new HashSet<Vertex>());
                graph.get(v).add(course);
            }
        }
        Vertex[] vertices = new Vertex[vertexSet.size()];
        int i = 0;
        for(Vertex v: vertexSet)
            vertices[i++] = v;
        topSortUsingDFS(graph, vertices);
        Arrays.sort(vertices);
        for (Vertex v : vertices)
            System.out.println(v);
    }

    public static double findMedian(int[] list1, int[] list2){
        int i = 0, j = 0, m1 = -1, m2 = -1, m = list1.length, n = list2.length, count = 0;
        if((m+n) % 2== 1){
            for(count = 0; count <= (n+m)/2; count++){
                if(i != n && j != m){
                    m1 = (list1[i] > list2[j]) ?
                            list2[j++] : list1[i++];
                }else if(i < n){
                    m1 = list1[i++];
                }
                else{
                    m1 = list2[j++];
                }
            }
            return m1;
        }
        else{
            for(count = 0;
                count <= (n + m) / 2;
                count++){

                m2 = m1;
                if(i != n && j != m){
                    m1 = (list1[i] > list2[j]) ?
                            list2[j++] : list1[i++];
                } else if(i<n){
                    m1 = list1[i++];
                }else{
                    m1 = list2[j++];
                }
            }
            return (m1 + m2) / 2;
        }
    }

    public static int median(int[] arr, int start, int end) {
        int n = end - start + 1;
        if (n % 2 == 0) {
            return (arr[start + (n / 2)] + arr[start + (n / 2 - 1)]) / 2;
        }
        else {
            return arr[start + n / 2];
        }
    }

    public static boolean getIsland(String[] islands, HashMap<String, List<String>> bridges, String srcIsland, String desIsland) {
        DisjointSet<String> ds = new DisjointSet<String>(islands);
        for (String from : bridges.keySet()) {
            for (String to : bridges.get(from)) {
                ds.union(from, to);
            }
        }
        return ds.find(srcIsland) == ds.find(desIsland);
    }

    private static void addEdge(Vertex from, Vertex to, HashMap<Vertex, Set<Vertex>> graph) {
        if (!graph.containsKey(from))
            graph.put(from, new HashSet<Vertex>());
        graph.get(from).add(to);
        to.indegree++;
    }

    public static void topSortUsingDFS(HashMap<Vertex, Set<Vertex>> graph, Vertex[] vertices) {
        LinkedList<Vertex> starters = new LinkedList<Vertex>();
        for (Vertex v : vertices)
            if (v.indegree == 0)
                starters.add(v);
        Stack<Vertex> stack = new Stack<Vertex>();
        int time = 0;
        int topNum = vertices.length;
        for (Vertex startVertex : starters) {
            stack.push(startVertex);
            while (!stack.empty()) {
                Vertex current = stack.pop();
                if (current.finished > 0)
                    continue;
                if (current.processed) {
                    current.finished = ++time;
                    current.topNum = topNum--;
                    continue;
                }
                current.discovered = ++time;
                if (!graph.containsKey(current)) {// out-degree = 0
                    current.finished = ++time;
                    current.topNum = topNum--;
                    continue;
                }
                current.processed = true;
                stack.push(current);
                for (Vertex neighbor : graph.get(current))
                    if (neighbor.discovered == 0) {
                        neighbor.pred = current;
                        stack.push(neighbor);
                    }
            }
        }
    }
}
