# Age Sorting Algorithm Analysis

[中文说明](年龄排序分析说明.md)

## Problem Statement

How to sort 1 million users by age? Assume the age range is from 1 to 120 years old.

## Solution

This program implements three different sorting algorithms in C and analyzes their execution efficiency.

## Three Implemented Sorting Algorithms

### 1. Counting Sort

**Time Complexity:** O(n + k), where n is the number of users and k is the age range (120)  
**Space Complexity:** O(n + k)  
**Stability:** Stable

**Pros:**
- Extremely efficient for sorting integers with limited range
- Best performance for this specific scenario
- Linear time complexity

**Cons:**
- Requires extra space
- Only suitable for integers with small range

### 2. Quick Sort

**Time Complexity:** Average O(n log n), Worst O(n²)  
**Space Complexity:** O(log n) (recursion stack)  
**Stability:** Unstable

**Pros:**
- General-purpose algorithm suitable for various scenarios
- Good average performance
- In-place sorting with high space efficiency

**Cons:**
- Poor worst-case performance
- Unstable
- Not optimal for this specific scenario

### 3. Radix Sort

**Time Complexity:** O(d × (n + k)), where d is number of digits and k is radix (10)  
**Space Complexity:** O(n + k)  
**Stability:** Stable

**Pros:**
- Very efficient for fixed-digit integers
- Stable sort
- Non-comparison based

**Cons:**
- Requires extra space
- Only applicable to integers or data convertible to integers

## Build and Run

### Prerequisites

- GCC compiler
- Make (optional, but recommended)

### Using Make (Recommended)

```bash
# Compile the program
make

# Compile and run
make run

# Clean compiled files
make clean

# Rebuild
make rebuild

# Show help
make help
```

### Manual Compilation

```bash
gcc -o age_sort_analysis age_sort_analysis.c -O2
./age_sort_analysis
```

## Performance Analysis Results

The program outputs:
1. Sample data before and after sorting for each algorithm
2. Execution time for each algorithm
3. Verification of sorting correctness
4. Detailed performance analysis summary

## Conclusion

For this problem (1 million users, age range 1-120):

1. **Counting Sort is the optimal choice**
   - Time complexity close to O(n)
   - Takes full advantage of the small age range
   - Fastest execution speed

2. **Radix Sort is second best**
   - Better performance than Quick Sort
   - Stable sorting
   - Suitable for integer sorting

3. **Quick Sort performs adequately**
   - Classic algorithm
   - But not optimal for this scenario
   - O(n log n) complexity is higher than Counting Sort

## Algorithm Selection Guidelines

- **Small and known data range**: Use Counting Sort
- **Need stable sorting**: Use Counting Sort or Radix Sort
- **General scenarios**: Use Quick Sort
- **Very large datasets**: Consider external or distributed sorting

## Project Structure

```
.
├── age_sort_analysis.c          # Main program source code
├── Makefile                      # Build automation script
├── 年龄排序分析说明.md            # Chinese documentation
└── AGE_SORTING_README.md         # This file (English documentation)
```

## Key Technical Points

1. **Memory Management**: Dynamic allocation using malloc/free
2. **Performance Measurement**: Using clock() function to measure execution time
3. **Result Verification**: Implemented sorting correctness verification function
4. **Code Reuse**: Testing framework using function pointers

## Further Considerations

1. If the age range expands to 1-10000, which algorithm would be better?
2. How to modify for dual sorting by age and name?
3. What optimizations are needed if data size reaches 100 million?
4. How much performance improvement can multi-threading bring?

## License

This project is part of the GitHub Skills learning repository and follows the repository's license.
