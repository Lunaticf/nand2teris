## VMTranslator
我们知道像Java编译的时候会生成bytecode，这种字节码
会在JVM这样的虚拟机上运行。实现虚拟机的一种方式是把这种中间形式再转换为machine code，
在目标机器上实际运行。

这里我们就是这样做的，把我们设计的中间代码翻译成我们的Hack汇编语言。

将分为两个部分，第一部分实现基本的VMTranslator的栈操作和算术、访问操作，第二部分将扩展成全
功能的VMTranslator，可以在Tag里看到第一部分。

## part-1
Stage I: Handling stack arithmetic commands
Stage II: Handling memory access commands

```
// 从栈弹出两个数操作 放回栈
add/sub/or/and:
@SP
AM=M-1  // M得到栈指针位置  -1指向第一个数
D=M     // 获取第一个数
A=A-1   // A指向第二个数
M=M+D/M-D/M|D/M&D   // 直接用结果覆盖第二个数

// 从栈弹出一个数操作
not/neg
@SP
A=M-1  // M得到栈指针位置  -1指向第一个数
D=0 // for neg
M=!M/D-M   // 取反

// 从栈弹出两个数操作 true - 1/false 0放回栈
eq/gt/lt
@SP
AM=M-1  // M得到栈指针位置  -1指向第一个数
D=M     // 获取第一个数
A=A-1   // A指向第二个数
D=M-D
// 根据D的值来跳转
@TRUE
D;JEQ/JGT/JLT // D=0/D>0/D<0时跳转到TRUE 默认放FALSE
// 这里A已经变了 不能直接M=-1
@SP
A=M-1
M=0
@CONTINUE
0;JMP
(TRUE)
@SP
A=M-1
M=-1
(CONTINUE)


// push/pop操作
// 先计算出要操作的地址
// 如果是temp或static或者pointer 直接+偏移即可 其他段需要多取一次再加偏移
@segment
D=A
@offset
A=D+A

// indirect segment
@segment
D=M
@offset
A=D+A

 

```
