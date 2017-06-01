import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.Formatter;

public class Dijkshtra {
    public static void main(String[] args) {
    	int noofvertices, noofedges;
        String tempstr = "";
        String[] tempStrArr1 = new String[2];
        String[] tempStrArr2 = new String[2];
        BufferedReader br;
      
        if(args.length != 1)
        {
            System.out.print("This program requires 1 argument (Input File Name) i.e.  java Dijkshtra Input.txt");
            System.exit(1);
        }
       
       
        try 
        {
            br = new BufferedReader(new FileReader(args[0]));
         
            tempstr =  br.readLine();
            tempStrArr1 = tempstr.split("\\s+");
            noofvertices = Integer.parseInt(tempStrArr1[0]); noofedges = Integer.parseInt(tempStrArr1[1]);
            Graph graph = new Graph(noofvertices);
            for (int i = 0; i < noofvertices;i++) 
            {
                graph.addVertex(i);
            }
            for (int i = 0; i < noofedges; i++) {
            	tempstr = br.readLine();
                tempStrArr2 = tempstr.split("\\s+");
                graph.addEdge(Integer.parseInt(tempStrArr2[0]),Integer.parseInt(tempStrArr2[1]),Integer.parseInt(tempStrArr2[2]));
            }
            graph.findShortestPaths(0);
            br.close(); 
        } 
        catch (FileNotFoundException ex)
        {
            System.out.println("File Not Found. Please enter correct input file name.");
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.out.println("IOException in readGraph: " + ex.getMessage());
            System.exit(1);
        }
        
      }

    public static class Graph 
    {
        Vertex[] vertices;
        int maxlength;
        int size;

        public Graph(int maxlength) {
            this.maxlength = maxlength;
            vertices = new Vertex[maxlength];
        }

        public void addVertex(int name) {
            vertices[size++] = new Vertex(name);
        }

        public void addEdge(int sourceName, int destinationName, int weight) {
            int srcIndex = sourceName;
            int destiIndex = destinationName;
            vertices[srcIndex].adj = new AdjacentNeighbour(destiIndex, weight, vertices[srcIndex].adj);
            vertices[destiIndex].indegree++;
        }
        
        public void findShortestPaths(int sourceName){
            applyDijkshtraAlgorithm(vertices[sourceName]);
            System.out.print("Vertex     ");
           
            for(int i = 0; i < maxlength; i++)
            {  
            	System.out.format("%10d ",vertices[i].name);
            }
            System.out.print("\nDistance   ");
            for(int i = 0; i < maxlength; i++)
            {   
            	System.out.format("%10d ",vertices[i].cost);
            }
            
            System.out.println("\n");
        }
        
        public class Vertex {
            int cost;
            int name;
            AdjacentNeighbour adj;
            int indegree;
            State state;

            public Vertex(int name) {
                this.name = name;
                
                cost = Integer.MAX_VALUE;
                state = State.NEWVERTEX;
            }

            public int compareTo(Vertex v) {
                if (this.cost == v.cost) {
                    return 0;
                }
                if (this.cost < v.cost) {
                    return -1;
                }
                return 1;
            }
        }

        public enum State {
            NEWVERTEX, VERTEX_IN_Q, VISITEDVERTEX
        }

        public class AdjacentNeighbour {
            int index;
            AdjacentNeighbour next;
            int weight;

            AdjacentNeighbour(int index, int weight, AdjacentNeighbour next) {
                this.index = index;
                this.next = next;
                this.weight = weight;
            }
        }

        public void applyDijkshtraAlgorithm(Vertex src) {
            Priority_Queue pq = new Priority_Queue(maxlength);
            pq.add(src);
            src.state = State.VERTEX_IN_Q;
            src.cost = 0;
            while (!pq.isEmpty()) {
                Vertex u = pq.remove();
                u.state = State.VISITEDVERTEX;
                AdjacentNeighbour temp = u.adj;
                while (temp != null) {
                    if (vertices[temp.index].state == State.NEWVERTEX) {
                        pq.add(vertices[temp.index]);
                        vertices[temp.index].state = State.VERTEX_IN_Q;
                    }
                    if (vertices[temp.index].cost > u.cost + temp.weight) {
                        vertices[temp.index].cost = u.cost + temp.weight;
                        pq.UPheapify(vertices[temp.index]);
                    }
                    temp = temp.next;
                }
            }
        }

        public static class Priority_Queue {
            private Vertex[] heap;
            private int maxlength;
            private int size;

            public Priority_Queue(int maxlength) {
                this.maxlength = maxlength;
                heap = new Vertex[maxlength];
            }

            public void add(Vertex u) {
                heap[size++] = u;
                UPheapify(size - 1);
            }

            public void UPheapify(Vertex u) {
                for (int i = 0; i < maxlength; i++) {
                    if (u == heap[i]) {
                        UPheapify(i);
                        break;
                    }
                }
            }

            public void UPheapify(int position) {
                int currentIndex = position;
                Vertex currentValue = heap[currentIndex];
                int parentIndex = (currentIndex - 1) / 2;
                Vertex parentValue = heap[parentIndex];
                while (currentValue.compareTo(parentValue) == -1) {
                    swap(currentIndex, parentIndex);
                    currentIndex = parentIndex;
                    if (currentIndex == 0) {
                        break;
                    }
                    currentValue = heap[currentIndex];
                    parentIndex = (currentIndex - 1) / 2;
                    parentValue = heap[parentIndex];
                }
            }

            public Vertex remove() {
                Vertex v = heap[0];
                swap(0, size - 1);
                heap[size - 1] = null;
                size--;
                Downheapify(0);
                return v;
            }

            public void Downheapify(int postion) 
            {
                if (size == 1) 
                {
                    return;
                }

                int currentIndex = postion;
                Vertex currentValue = heap[currentIndex];
                int leftChildIndex = 2 * currentIndex + 1;
                int rightChildIndex = 2 * currentIndex + 2;
                int childIndex;
                if (heap[leftChildIndex] == null) 
                {
                    return;
                }
                if (heap[rightChildIndex] == null) 
                {
                    childIndex = leftChildIndex;
                } 
                else if (heap[rightChildIndex].compareTo(heap[leftChildIndex]) == -1) 
                {
                    childIndex = rightChildIndex;
                } 
                else 
                {
                    childIndex = leftChildIndex;
                }
                Vertex childValue = heap[childIndex];
                while (currentValue.compareTo(childValue) == 1) 
                {
                    swap(currentIndex, childIndex);
                    currentIndex = childIndex;
                    currentValue = heap[currentIndex];
                    leftChildIndex = 2 * currentIndex + 1;
                    rightChildIndex = 2 * currentIndex + 2;
                    if (heap[leftChildIndex] == null) 
                    {
                        return;
                    }
                    if (heap[rightChildIndex] == null) 
                    {
                        childIndex = leftChildIndex;
                    } 
                    else if (heap[rightChildIndex].compareTo(heap[leftChildIndex]) == -1) 
                    {
                        childIndex = rightChildIndex;
                    } 
                    else 
                    {
                        childIndex = leftChildIndex;
                    }
                }
            }

            public void swap(int index1, int index2) {
                Vertex temp = heap[index1];
                heap[index1] = heap[index2];
                heap[index2] = temp;
            }

            public boolean isEmpty() {

                return size == 0;
            }
        }
    }
}
