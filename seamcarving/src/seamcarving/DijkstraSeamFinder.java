package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPathFinder;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<DN, Edge<DN>>, DN, Edge<DN>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    private static class DN {
        //private final double weightPixel;
        private final int coordX;
        private final int coordY;

        public DN(int x, int y) {
            //this.weightPixel = weight;
            this.coordX = x;
            this.coordY = y;

        }

        /*whatever methods you want here*/

        // You should probably implement equals and hashCode:

        public boolean equals(Object other) {
            if (!(other instanceof DN)) {
                return false;
            }

            DN o = (DN) other;
            // compare o and this below
            // if(o.weightPixel > this.weightPixel) {
            //     return
            // }
            return this.coordX == o.coordX && this.coordY == o.coordY;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordX, coordY);
        }
    }
    private static class Giraffe implements graphs.Graph<DN, Edge<DN>>  {
        // Note: use "static" in the class header if it doesn't use any non-static fields
        // in the outer class. Otherwise, remove the static keyword.
        //private Map<DN, Set<Edge<DN>>> giraffeList;
        private DN to;
        private DN from;

        private double[][] energiesList;
        private DN dummy;
        private DN dummyEnd;
        // fields

        public Giraffe(double[][] energies) {
            //this.giraffeList = new HashMap<>();
            this.energiesList = energies;
            this.dummy = new DN(-1, -1);
            this.dummyEnd = new DN(-100, -100);
            // initialize fields
            // Note: you don't need to create an actual adjacency list
            // for (Edge<DN> edge: edges) {
            //     setOfEdge.add(edge);
            //     adjList.putIfAbsent(edge.to(), new HashSet<Edge<DN>>());
            //     if (adjList.containsKey(edge.to())) {
            //         adjList.put(edge.to(), .add())
            //     }
            // }
        }

        @Override
        public Collection<Edge<DN>> outgoingEdgesFrom(DN vertex) {
            Set<Edge<DN>> returnSet = new HashSet<>();
            int amount = 0;

            if (vertex.coordX == -1) {
                for (int i = 0; i < energiesList.length; i++) {
                    returnSet.add(new Edge<>(vertex, new DN(i, 0), energiesList[i][0]));
                }
            } else if (vertex.coordY == energiesList[0].length-1) {
                returnSet.add(new Edge<>(vertex, dummyEnd, 0.0));
            } else if (vertex.coordX == 0) {
                returnSet.add(new Edge<>(vertex, new DN(0, vertex.coordY + 1), energiesList[0][vertex.coordY + 1]
                ));
                returnSet.add(new Edge<>(vertex, new DN(1, vertex.coordY +1), energiesList[1][vertex.coordY + 1]));
            } else if (vertex.coordX == energiesList.length-1) {
                returnSet.add(new Edge<>(vertex, new DN(vertex.coordX, vertex.coordY+1), energiesList[vertex.coordX]
                    [vertex.coordY + 1]));
                returnSet.add(new Edge<>(vertex, new DN(vertex.coordX - 1, vertex.coordY+1), energiesList
                    [vertex.coordX -1]
                    [vertex.coordY + 1]));
            } else if (vertex.coordX >= 0) {
                returnSet.add(new Edge<>(vertex, new DN(vertex.coordX + 1, vertex.coordY + 1), energiesList
                    [vertex.coordX + 1]
                    [vertex.coordY + 1]));
                returnSet.add(new Edge<>(vertex, new DN(vertex.coordX, vertex.coordY + 1), energiesList
                    [vertex.coordX]
                    [vertex.coordY + 1]));
                returnSet.add(new Edge<>(vertex, new DN(vertex.coordX - 1, vertex.coordY + 1), energiesList
                    [vertex.coordX - 1]
                    [vertex.coordY + 1]));
            }
            return returnSet;
        }

        // helper methods here
    }

    private static class Giraffe2 implements graphs.Graph<DN, Edge<DN>>  {
        // Note: use "static" in the class header if it doesn't use any non-static fields
        // in the outer class. Otherwise, remove the static keyword.
        //private Map<DN, Set<Edge<DN>>> giraffeList;
        private DN to;
        private DN from;

        private double[][] energiesList;
        private DN dummy;
        private DN dummyEnd;
        // fields

        public Giraffe2(double[][] energies) {
            //this.giraffeList = new HashMap<>();
            this.energiesList = energies;
            this.dummy = new DN(-1, -1);
            this.dummyEnd = new DN(-100, -100);
        }

        @Override
        public Collection<Edge<DN>> outgoingEdgesFrom(DN vertex1) {
            Set<Edge<DN>> returnSet = new HashSet<>();
            if (vertex1.coordX == -1) {
                for (int i = 0; i < energiesList[0].length; i++) {
                    returnSet.add(new Edge<>(vertex1, new DN(0, i), energiesList[0][i]));
                }
            } else if (vertex1.coordX == energiesList.length-1) {
                returnSet.add(new Edge<>(vertex1, dummyEnd, 0.0));
            } else if (vertex1.coordY == 0) {
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX+1, vertex1.coordY + 1),
                    energiesList[vertex1.coordX+1]
                    [vertex1.coordY + 1]));
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX+1, vertex1.coordY),
                    energiesList[vertex1.coordX+1]
                    [vertex1.coordY]));
            } else if (vertex1.coordY == energiesList[0].length-1) {
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX+1, vertex1.coordY), energiesList
                    [vertex1.coordX+1][vertex1.coordY]));
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX + 1, vertex1.coordY-1), energiesList
                    [vertex1.coordX +1]
                    [vertex1.coordY - 1]));
            } else if (vertex1.coordX >= 0) {
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX + 1, vertex1.coordY + 1), energiesList
                    [vertex1.coordX + 1]
                    [vertex1.coordY + 1]));
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX + 1, vertex1.coordY), energiesList
                    [vertex1.coordX+1]
                    [vertex1.coordY]));
                returnSet.add(new Edge<>(vertex1, new DN(vertex1.coordX + 1, vertex1.coordY - 1), energiesList
                    [vertex1.coordX + 1]
                    [vertex1.coordY - 1]));
            }
            return returnSet;
        }

        // helper methods here
    }
    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        Set<DN> paramSet = new HashSet<>();
        //List<DN> verts = new ArrayList<>();
        //DN curr;
        List<Integer> returnList = new ArrayList<>();
        Giraffe2 horizontal2 = new Giraffe2(energies);
        // createPathFinder();
        // ShortestPathFinder<Graph<DN, Edge<DN>>, DN, Edge<DN>> pathFinder = createPathFinder();
        for (DN verti : pathFinder.findShortestPath(horizontal2, horizontal2.dummy, horizontal2.dummyEnd).vertices()) {
            if (verti.coordX >= 0) {
                returnList.add(verti.coordY);
            }
        }
        return returnList;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        Giraffe horizontal = new Giraffe(energies);
        List<Integer> returnList = new ArrayList<>();
        // createPathFinder();
        // ShortestPathFinder<Graph<DN, Edge<DN>>, DN, Edge<DN>> pathFinder = createPathFinder();
        // double currWeight = Double.POSITIVE_INFINITY;
        // int lowest = 0;
        // for (int i = 0; i < energies.length; i++) {
        //     if (pathFinder.findShortestPath(horizontal, verts.get(i), dummyNode).totalWeight() < currWeight) {
        //         currWeight = pathFinder.findShortestPath(horizontal, verts.get(i), dummyNode).totalWeight();
        //         lowest = i;
        //     }
        // }
        for (DN vertices : pathFinder.findShortestPath(horizontal, horizontal.dummy, horizontal.dummyEnd).vertices()) {
            if (vertices.coordY >= 0) {
                returnList.add(vertices.coordX);
            }
        }
        return returnList;
    }
}
