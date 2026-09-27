# Assignment 2: Algorithmic Analysis, Correctness and Performance Trade-offs

## 1. Overview
This repository contains the implementation and analysis of three fundamental data structures implemented from scratch in Java:
1. **Dynamic Array** (`DynamicArray.java`)
2. **Singly Linked List** (`LinkedList.java`)
3. **Min-Heap** (`MinHeap.java`)

The objective of this assignment is to conduct formal algorithmic complexity analysis using asymptotic notation (O, Omega, Theta), prove the correctness of key operations using **loop invariants**, execute empirical benchmark workloads, and compare theoretical predictions against measured performance.

---

## 2. Complexity Analysis

| Data Structure | Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Dynamic Array** | `get(i)` | Theta(1) | Theta(1) | Theta(1) | O(1) | Direct array offset calculation via memory address. |
| | `add(x)` | Theta(1) | Theta(1) | Theta(n) | O(1) | Amortized Theta(1); worst-case Theta(n) occurs during array resizing. |
| | `add(i, x)` | Theta(1) | Theta(n) | Theta(n) | O(1) | Appending at tail is Theta(1); inserting at index 0 requires shifting n elements. |
| | `remove(i)`| Theta(1) | Theta(n) | Theta(n) | O(1) | Removing last element is Theta(1); index 0 requires shifting n - 1 elements. |
| | `contains(x)`| Theta(1) | Theta(n) | Theta(n) | O(1) | Linear scan through the array elements. |
| **Linked List** | `get(i)` | Theta(1) | Theta(n) | Theta(n) | O(1) | Accessing index 0 takes Theta(1); accessing index n - 1 requires traversing n nodes. |
| | `add(x)` | Theta(1) | Theta(1) | Theta(1) | O(1) | Maintained tail pointer allows constant-time append operations. |
| | `add(i, x)` | Theta(1) | Theta(n) | Theta(n) | O(1) | Head insertion (i = 0) is Theta(1); middle/tail insertion requires node traversal. |
| | `remove(i)`| Theta(1) | Theta(n) | Theta(n) | O(1) | Head removal is Theta(1); other indices require sequential node traversal. |
| | `contains(x)`| Theta(1) | Theta(n) | Theta(n) | O(1) | Linear traversal through linked node references. |
| **Min-Heap** | `insert(x)` | Theta(1) | Theta(log n) | Theta(log n) | O(1) | Appends element to end and calls siftUp. Best case occurs when x >= parent. |
| | `peekMin()` | Theta(1) | Theta(1) | Theta(1) | O(1) | Minimum element is always located at the root index 0. |
| | `extractMin()`| Theta(log n) | Theta(log n) | Theta(log n) | O(1) | Replaces root with last element and restores heap order via siftDown. |

---

## 3. Algorithmic Correctness & Loop Invariants

### Proof 1: contains(x) in DynamicArray
- **Loop Invariant:** At the start of iteration i (0 <= i <= size), element x is not present in data[0 ... i - 1].
- **Initialization:** At i = 0, data[0 ... -1] is empty, so x is trivially not found. The invariant holds.
- **Maintenance:** If data[i] equals x, the method returns true. If data[i] != x, combined with x not in data[0 ... i - 1], then x is not in data[0 ... i]. Incrementing i preserves the invariant for the next iteration.
- **Termination:** The loop terminates when i = size. By the invariant, x is not present in data[0 ... size - 1].
- **Conclusion:** Searching the entire array without finding x proves that returning false is correct.

---

### Proof 2: siftUp(k) in MinHeap
- **Loop Invariant:** For all nodes j != k, heap[j] >= heap[(j - 1) / 2]. All subtrees below k satisfy min-heap properties.
- **Initialization:** Initially, k = size - 1. The prior structure was a valid heap, so only the edge between k and its parent may violate the property. The invariant holds.
- **Maintenance:** If heap[k] >= heap[parent], the loop breaks. If heap[k] < heap[parent], swap(k, parent) fixes the local violation and moves k to parent. The invariant holds for the new position k.
- **Termination:** The loop terminates when k = 0 (root reached) or heap[k] >= heap[parent].
- **Conclusion:** All parent-child relationships satisfy the min-heap order upon termination.

---

## 4. Experimental Setup
- **Input Sizes (n):** 100, 1,000, 10,000, 100,000
- **Repetitions:** 5 runs per test (averaged)
- **Timing:** System.nanoTime()
- **Random Seed:** Random(42)
- **Workloads:**
    1. **Workload 1 (Random Access):** 10,000 get(index) operations on Dynamic Array vs. Linked List.
    2. **Workload 2 (Search):** 1,000 contains(value) searches.
    3. **Workload 3 (Insertion & Removal):** 1,000 inserts/removals at index 0 and index n/2.
    4. **Workload 4 (Priority Processing):** n insertions followed by n extractMin() operations in Min-Heap.

---

## 5. Results & Discussion

- Raw measurements are saved in `results/results.csv`.
- Generated plots are saved in `results/plots/`.

### Discussion Points
1. **Impact of n:** Linear operations (LinkedList.get, array element shifting) slow down significantly with larger n. Array random access stays O(1), while Min-Heap scales at O(log n).
2. **Theory vs. Practice:** Empirical trends match O(1) for array indexing and O(n) for sequential searching/traversals.
3. **Deviations:** Small-array operations in DynamicArray run faster than pure Big-O predictions due to native CPU memory copying via System.arraycopy.
4. **Big-O Equivalence:** Algorithms with the same Big-O can differ in runtime due to hidden constant factors and CPU caching.
5. **Hardware Effects:** Contiguous memory in arrays optimizes L1/L2 CPU cache utilization (Spatial Locality), whereas Linked List node pointers cause frequent cache misses.
6. **Dynamic Array:** Best for index lookups, iterations, and append operations.
7. **Linked List:** Best for operations restricted strictly to head insertions/deletions O(1).
8. **Min-Heap:** Ideal for priority processing, enabling O(log n) access to extreme values without full sorting O(n log n).
9. **Selection Strategy:** Match data structures to primary workload patterns (random access -> array; priority -> heap; head updates -> linked list).

---

## 6. How to Run

```bash
# Compile and run unit tests
javac -d bin src/*.java
java -cp bin Tests

# Run benchmarks
java -cp bin Benchmark

# Generate plots
pip install pandas matplotlib
python3 src/generate_plots.py