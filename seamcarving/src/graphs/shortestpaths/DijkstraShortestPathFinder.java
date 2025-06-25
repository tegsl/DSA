package graphs.shortestpaths;

//import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
//import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        //return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        ExtrinsicMinPQ<E> minP = createMinPQ();
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> edgeTo = new HashMap<>();
        Set<V> found = new HashSet<>();
        // Set<V> fullList = new HashSet<>();

        distTo.put(start, 0.0);
        if (Objects.equals(start, end)) {
            return edgeTo;
        }
        if (graph.outgoingEdgesFrom(start).isEmpty()) {
            return edgeTo;
        }
        for (E edge : graph.outgoingEdgesFrom(start)) {
            minP.add(edge, edge.weight());
            //fullList.add(edge.to());
        }
        //fullList.add(start);
        if (minP.size() > 0) {
            while ((!found.contains(start) || !found.contains(end))) {
                if (minP.isEmpty()) {
                    return edgeTo;
                }
                E removed = minP.removeMin();
                found.add(removed.to());
                if (Objects.equals(distTo.get(removed.to()), null)) {
                        distTo.put(removed.to(), removed.weight() + distTo.get(removed.from()));
                        edgeTo.put(removed.to(), removed);
                } else if (Objects.equals(removed.from(), start) && distTo.get(removed.to()) > removed.weight()) {
                    distTo.put(removed.to(), removed.weight());
                    edgeTo.put(removed.to(), removed);
                }
                for (E edge : graph.outgoingEdgesFrom(removed.to())) {
                    //fullList.add(edge.to());
                    if (!found.contains(edge.to()) && !minP.contains(edge)) {
                        minP.add(edge, edge.weight()+distTo.get(edge.from()));
                    }
                    if (!distTo.containsKey(edge.to())) {
                        double newDist = edge.weight() + distTo.get(edge.from());
                        if (newDist < Double.POSITIVE_INFINITY) {
                            distTo.put(edge.to(), newDist);
                            edgeTo.put(edge.to(), edge);
                        } else {
                            distTo.put(edge.to(), Double.POSITIVE_INFINITY);
                            edgeTo.put(edge.to(), edge);
                        }
                    } else {
                        double newDist = edge.weight() + distTo.get(edge.from());
                        double oldDist = distTo.get(edge.to());
                        if (newDist < oldDist) {
                            distTo.put(edge.to(), newDist);
                            edgeTo.put(edge.to(), edge);

                        }
                    }
                }
            }
        }

        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (Objects.equals(spt, null)) {
            return new ShortestPath.Failure<>();
        }
        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }
        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        List<E> returnList= new ArrayList<>();
        int i = 0;
        while (edge != null) {
            returnList.add(i, edge);
            // if (Objects.equals(returnList.get(i).from(), start)) {
            //     Collections.reverse(returnList);
            //
            //     return new ShortestPath.Success<>(returnList);
            // }
            i++;
            edge = spt.get(edge.from());
        }
        // return new ShortestPath.Success<>()
        Collections.reverse(returnList);
        return new ShortestPath.Success<>(returnList);

    }

}
