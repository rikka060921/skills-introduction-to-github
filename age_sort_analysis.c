/**
 * 年龄排序算法分析程序
 * 
 * 问题：如何根据年龄给 100 万用户排序？
 * 假设年龄范围：1-120 岁
 * 
 * 本程序实现并比较三种排序算法的执行效率：
 * 1. 计数排序 (Counting Sort) - 针对有限范围的最优算法
 * 2. 快速排序 (Quick Sort) - 通用比较排序算法
 * 3. 基数排序 (Radix Sort) - 非比较排序算法
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define USER_COUNT 1000000  // 100万用户
#define MIN_AGE 1           // 最小年龄
#define MAX_AGE 120         // 最大年龄
#define AGE_RANGE (MAX_AGE - MIN_AGE + 1)

/**
 * 用户结构体
 */
typedef struct {
    int id;    // 用户ID
    int age;   // 年龄
} User;

/**
 * 生成随机年龄数据
 */
void generate_random_ages(User *users, int count) {
    for (int i = 0; i < count; i++) {
        users[i].id = i + 1;
        users[i].age = MIN_AGE + rand() % AGE_RANGE;
    }
}

/**
 * 复制用户数组
 */
void copy_users(User *dest, User *src, int count) {
    memcpy(dest, src, count * sizeof(User));
}

/**
 * 验证排序结果是否正确
 */
int verify_sorted(User *users, int count) {
    for (int i = 1; i < count; i++) {
        if (users[i].age < users[i-1].age) {
            return 0; // 排序错误
        }
    }
    return 1; // 排序正确
}

/**
 * 打印数组样本
 */
void print_sample(User *users, int count, int sample_size) {
    printf("  前 %d 个用户: ", sample_size);
    for (int i = 0; i < sample_size && i < count; i++) {
        printf("(ID:%d,年龄:%d) ", users[i].id, users[i].age);
    }
    printf("\n");
}

// ==================== 算法1: 计数排序 ====================

/**
 * 计数排序 (Counting Sort)
 * 
 * 时间复杂度: O(n + k)，其中 n 是用户数，k 是年龄范围
 * 空间复杂度: O(n + k)
 * 稳定性: 稳定排序
 * 
 * 优点：对于有限范围的整数排序非常高效
 * 缺点：需要额外空间，不适合范围很大的数据
 */
void counting_sort(User *users, int count) {
    // 创建计数数组
    int *count_array = (int *)calloc(AGE_RANGE, sizeof(int));
    User *output = (User *)malloc(count * sizeof(User));
    
    // 统计每个年龄的出现次数
    for (int i = 0; i < count; i++) {
        count_array[users[i].age - MIN_AGE]++;
    }
    
    // 计算累积计数（前缀和）
    for (int i = 1; i < AGE_RANGE; i++) {
        count_array[i] += count_array[i - 1];
    }
    
    // 从后向前遍历，保证稳定性
    for (int i = count - 1; i >= 0; i--) {
        int age_index = users[i].age - MIN_AGE;
        output[count_array[age_index] - 1] = users[i];
        count_array[age_index]--;
    }
    
    // 将结果复制回原数组
    memcpy(users, output, count * sizeof(User));
    
    free(count_array);
    free(output);
}

// ==================== 算法2: 快速排序 ====================

/**
 * 快速排序的分区函数
 */
int partition(User *users, int low, int high) {
    int pivot = users[high].age;
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (users[j].age <= pivot) {
            i++;
            User temp = users[i];
            users[i] = users[j];
            users[j] = temp;
        }
    }
    
    User temp = users[i + 1];
    users[i + 1] = users[high];
    users[high] = temp;
    
    return i + 1;
}

/**
 * 快速排序递归函数
 */
void quick_sort_recursive(User *users, int low, int high) {
    if (low < high) {
        int pi = partition(users, low, high);
        quick_sort_recursive(users, low, pi - 1);
        quick_sort_recursive(users, pi + 1, high);
    }
}

/**
 * 快速排序 (Quick Sort)
 * 
 * 时间复杂度: 平均 O(n log n)，最坏 O(n²)
 * 空间复杂度: O(log n) (递归栈)
 * 稳定性: 不稳定排序
 * 
 * 优点：平均性能优秀，原地排序
 * 缺点：最坏情况性能差，不稳定
 */
void quick_sort(User *users, int count) {
    quick_sort_recursive(users, 0, count - 1);
}

// ==================== 算法3: 基数排序 ====================

/**
 * 获取指定位的数字
 */
int get_digit(int num, int exp) {
    return (num / exp) % 10;
}

/**
 * 基数排序的计数排序（按位排序）
 */
void counting_sort_by_digit(User *users, int count, int exp) {
    User *output = (User *)malloc(count * sizeof(User));
    int count_array[10] = {0};
    
    // 统计每个数字的出现次数
    for (int i = 0; i < count; i++) {
        int digit = get_digit(users[i].age, exp);
        count_array[digit]++;
    }
    
    // 计算累积计数
    for (int i = 1; i < 10; i++) {
        count_array[i] += count_array[i - 1];
    }
    
    // 从后向前构建输出数组
    for (int i = count - 1; i >= 0; i--) {
        int digit = get_digit(users[i].age, exp);
        output[count_array[digit] - 1] = users[i];
        count_array[digit]--;
    }
    
    // 复制回原数组
    memcpy(users, output, count * sizeof(User));
    free(output);
}

/**
 * 基数排序 (Radix Sort)
 * 
 * 时间复杂度: O(d * (n + k))，d 是位数，k 是基数
 * 空间复杂度: O(n + k)
 * 稳定性: 稳定排序
 * 
 * 优点：对于固定位数的整数非常高效
 * 缺点：需要额外空间
 */
void radix_sort(User *users, int count) {
    // 找到最大年龄以确定位数
    int max_age = MAX_AGE;
    
    // 从个位开始，对每一位进行计数排序
    for (int exp = 1; max_age / exp > 0; exp *= 10) {
        counting_sort_by_digit(users, count, exp);
    }
}

// ==================== 性能测试主函数 ====================

/**
 * 测试单个排序算法的性能
 */
void test_sorting_algorithm(const char *name, void (*sort_func)(User*, int), 
                           User *original_users, int count) {
    printf("\n========== %s ==========\n", name);
    
    // 创建副本用于排序
    User *users = (User *)malloc(count * sizeof(User));
    copy_users(users, original_users, count);
    
    printf("排序前样本:\n");
    print_sample(users, count, 5);
    
    // 记录开始时间
    clock_t start = clock();
    
    // 执行排序
    sort_func(users, count);
    
    // 记录结束时间
    clock_t end = clock();
    double time_spent = ((double)(end - start)) / CLOCKS_PER_SEC;
    
    printf("排序后样本:\n");
    print_sample(users, count, 5);
    
    // 验证排序结果
    if (verify_sorted(users, count)) {
        printf("✓ 排序结果正确\n");
    } else {
        printf("✗ 排序结果错误！\n");
    }
    
    printf("执行时间: %.4f 秒\n", time_spent);
    
    free(users);
}

/**
 * 主函数
 */
int main() {
    printf("====================================================\n");
    printf("       年龄排序算法性能分析\n");
    printf("====================================================\n");
    printf("用户数量: %d\n", USER_COUNT);
    printf("年龄范围: %d - %d 岁\n", MIN_AGE, MAX_AGE);
    printf("====================================================\n");
    
    // 设置随机种子
    srand(time(NULL));
    
    // 生成随机用户数据
    printf("\n正在生成 %d 个用户的随机年龄数据...\n", USER_COUNT);
    User *users = (User *)malloc(USER_COUNT * sizeof(User));
    generate_random_ages(users, USER_COUNT);
    printf("数据生成完成！\n");
    
    // 测试三种排序算法
    test_sorting_algorithm("计数排序 (Counting Sort)", counting_sort, users, USER_COUNT);
    test_sorting_algorithm("快速排序 (Quick Sort)", quick_sort, users, USER_COUNT);
    test_sorting_algorithm("基数排序 (Radix Sort)", radix_sort, users, USER_COUNT);
    
    // 性能总结
    printf("\n====================================================\n");
    printf("                 性能分析总结\n");
    printf("====================================================\n");
    printf("1. 计数排序 (Counting Sort):\n");
    printf("   - 时间复杂度: O(n + k) ≈ O(n)\n");
    printf("   - 空间复杂度: O(n + k)\n");
    printf("   - 最适合本场景，因为年龄范围小(1-120)\n");
    printf("   - 稳定排序，性能最优\n\n");
    
    printf("2. 快速排序 (Quick Sort):\n");
    printf("   - 时间复杂度: O(n log n)\n");
    printf("   - 空间复杂度: O(log n)\n");
    printf("   - 通用排序算法，适合各种场景\n");
    printf("   - 不稳定排序，对本场景不是最优\n\n");
    
    printf("3. 基数排序 (Radix Sort):\n");
    printf("   - 时间复杂度: O(d * (n + k))\n");
    printf("   - 空间复杂度: O(n + k)\n");
    printf("   - 适合固定位数的整数排序\n");
    printf("   - 稳定排序，性能优于快速排序\n\n");
    
    printf("结论：对于年龄排序（范围1-120），计数排序是最优选择。\n");
    printf("====================================================\n");
    
    free(users);
    return 0;
}
