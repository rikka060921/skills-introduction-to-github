# Makefile for Age Sorting Algorithm Analysis
# 年龄排序算法分析编译脚本

CC = gcc
CFLAGS = -O2 -Wall -Wextra
TARGET = age_sort_analysis
SOURCE = age_sort_analysis.c

# 默认目标：编译程序
all: $(TARGET)

# 编译规则
$(TARGET): $(SOURCE)
	$(CC) $(CFLAGS) -o $(TARGET) $(SOURCE)
	@echo "编译成功！可以运行 ./$(TARGET) 或 make run"

# 运行程序
run: $(TARGET)
	./$(TARGET)

# 清理编译产物
clean:
	rm -f $(TARGET)
	@echo "清理完成！"

# 重新编译
rebuild: clean all

# 帮助信息
help:
	@echo "可用的命令："
	@echo "  make          - 编译程序"
	@echo "  make run      - 编译并运行程序"
	@echo "  make clean    - 清理编译产物"
	@echo "  make rebuild  - 重新编译"
	@echo "  make help     - 显示此帮助信息"

.PHONY: all run clean rebuild help
