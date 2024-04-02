package maze.algo.generation;

class Edge {

    /**
     * The coordinate of the first cell.
     */
    private final int firstCell;
    /**
     * The coordinate of the second cell.
     */
    private final int secondCell;

    /**
     * Creates a new edge with given cells coordinates.
     *
     * @param firstCell  the coordinate of the first cell
     * @param secondCell the coordinate of the second cell
     */
    Edge(int firstCell, int secondCell) {
        this.firstCell = firstCell;
        this.secondCell = secondCell;
    }

    /**
     * @return the first cell coordinate
     */
    int getFirstCell() {
        return firstCell;
    }

    /**
     * @return the second cell coordinate
     */
    int getSecondCell() {
        return secondCell;
    }
}
