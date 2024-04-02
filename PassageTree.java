package maze.algo.generation;

import maze.model.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static maze.model.Cell.Type.PASSAGE;


public class PassageTree {

    /**
     * The height of the maze in an imaginary edge form.
     */
    private int height;

    /**
     * The width of the maze in an imaginary edge form.
     */
    private int width;

    /**
     * Creates a new imaginary edge form
     *
     * @param height an original height
     * @param width  an original width
     */
    public PassageTree(int height, int width) {
        this.height = (height - 1) / 2;
        this.width = (width - 1) / 2;
    }

    /**
     * Generates a random list of cells that connect passages in
     * an original form such that a maze is simply connected.
     *
     * @return a random list of cells that connect passages
     */
    public List<Cell> generate() {
        var edges = createEdges();
        Collections.shuffle(edges);
        var tree = buildRandomSpanningTree(edges);
        return createPassages(tree);
    }

    /**
     * Creates a list of all possible edges in an imaginary edge form.
     *
     * @return a list of all possible edges
     * @see Edge
     */
    private List<Edge> createEdges() {
        var edges = new ArrayList<Edge>();
        for (int column = 1; column < width; column++) {
            edges.add(new Edge(toIndex(0, column),
                               toIndex(0, column - 1)));
        }
        for (int row = 1; row < height; row++) {
            edges.add(new Edge(toIndex(row, 0),
                               toIndex(row - 1, 0)));
        }
        for (int row = 1; row < height; row++) {
            for (int column = 1; column < width; column++) {
                edges.add(new Edge(toIndex(row, column),
                                   toIndex(row, column - 1)));
                edges.add(new Edge(toIndex(row, column),
                                   toIndex(row - 1, column)));
            }
        }
        return edges;
    }

    /**
     * Transforms the coordinates in a 2-dimensional array
     * to the coordinate in a 1-dimensional array using the
     * {@code row * width + column} formula.
     *
     * @param row    the row coordinate in a 2-dimensional array
     * @param column the column coordinate in a 2-dimensional array
     * @return the coordinate in a 1-dimensional array
     */
    private int toIndex(int row, int column) {
        return row * width + column;
    }

    private List<Edge> buildRandomSpanningTree(List<Edge> edges) {
        var disjointSets = new DisjointSet(width * height);
        return edges
            .stream()
            .filter(edge -> connects(edge, disjointSets))
            .collect(toList());
    }

    /**
     * Checks if an {@code edge} connects two disjoint subsets.
     *
     * @param edge        a given edge
     * @param disjointSet a disjoint-set data structure that keeps track of subsets
     * @return true if an edge connects two disjoint subsets,
     * false otherwise
     */
    private boolean connects(Edge edge, DisjointSet disjointSet) {
        return disjointSet.union(edge.getFirstCell(), edge.getSecondCell());
    }

    /**
     * Scales and converts edges in an imaginary edge form to the cells
     * which connect passages in a original form.
     *
     * @param spanningTree a random list of edges that connect passages
     * @return a list of cells that connect passages
     */
    private List<Cell> createPassages(List<Edge> spanningTree) {
        return spanningTree
            .stream()
            .map(edge -> {
                var first = fromIndex(edge.getFirstCell());
                var second = fromIndex(edge.getSecondCell());
                return getPassage(first, second);
            }).collect(toList());
    }

    /**
     * Transforms the coordinate in a 1-dimensional array
     * back to the coordinates in a 2-dimensional array using the
     * {@code row = index / width} and {@code column = index % width}
     * formulas.
     *
     * @param index the coordinate in a 1-dimensional array
     * @return a cell with coordinates in a 2-dimensional array
     */
    private Cell fromIndex(int index) {
        var row = index / width;
        var column = index % width;
        return new Cell(row, column, PASSAGE);
    }

    /**
     * Given the coordinates of two cells that compose an edge in
     * an imaginary edge form, it scales and transforms them to
     * the coordinates of the cell that connect passages in an
     * original form. Returns a passage cell with this coordinates.
     *
     * @param first  one edge ending
     * @param second another edge ending
     * @return a passage cell with the transformed coordinates
     */
    private Cell getPassage(Cell first, Cell second) {
        var row = first.getRow() + second.getRow() + 1;
        var column = first.getColumn() + second.getColumn() + 1;
        return new Cell(row, column, PASSAGE);
    }
}
