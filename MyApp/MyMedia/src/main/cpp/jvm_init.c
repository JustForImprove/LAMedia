//
// Created by 10415 on 2020/10/10.
//







/*
 * 1、普通变量定义成全局变量
     如果是普通类型，完全可以不用*.h文件，直接在*.c文件中定义，在调用文件处用extern 声明，因为对于普通类型，编译器是可以识别的。
     比如在一个 main.c文件中，我定义了int flag;那么在别的文件中只要用extern int flag外部声明就可以了，告诉编译器这个变量我已经定义过了，具体怎样，你慢慢找吧。这符合常理，因为int是编译器能自主识别的类型。
     2、自定义结构体类型定义成全局变量
     不同于普通类型，如果不预先通知编译器，编译器是不会识别你自定义的类型的。这个时候，*.h文件便出现了。不是定义结构类型不占内存吗？
     那好，我大结构体的定义放在*.h文件中，这样一来，无论你incude无数次，内存都不会被占用的。而且这样还有个好处，
     在别的文件中可以include这个*.h文件，这样，在这个文件中，编译器就可以识别你的自定义类型了，目的不就达到了？  假如我在mod.h中定义了
 *
 *
 *
 *
 *
 * 1.如何引用一个已经定义过的全局变量？
 * extern 可以用引用头文件的方式，也可以用extern关键字，如果用引用头文件方式来引用某个在头文件中声明的全局变理，
 * 假定你将那个编写错了，那么在编译期间会报错，如果你用extern方式引用时，假定你犯了同样的错误，那么在编译期间不会报错，而在连接期间报错。
 *要在头文件中定义有以下两种方法：用extern来声明:extern int flag;这一句只是对变量i进行声明，在c文件的程序之前必须加上int i进行定义。extern int flag=0;这一句声明和定义都做了。
有很多c文件和头文件，这个时候全局变量就必须在头文件中声明(不需要初始化)，然后在一个c文件中定义(该初始化的要初始化)。如果在头文件中定义，则编译的时候会出现重复定义的错误。
 如果只有头文件中声明就会出现没有定义有警告。
 *
 *2.全局变量可不可以定义在可被多个.C文件中？为什么？ 　　
可以，在不同的C文件中以static形式来声明同名全局变量。 可以在不同的C文件中声明同名的全局变量，前提是其中只能有一个C文件中对此变量赋初值，此时连接不会出错。
 *
 *3, 全局结构体只要把定义放在头文件中就可以了，声明在.c文件中
 *
 *与extern对应的关键字是static，被它修饰的全局变量和函数只能在本模块中使用。因此，一个函数或变量只可能被本模块使用时，其不可能被extern “C”修饰。
 */

#include <jni.h>


// 使用总结，头文件中不能定义，只能声明
// 第一个.C文件必须逐个extern，不能引入整体，因为这样会导致没有定义错误
// 总之， #include是引入代码，那么第一个引入的就必须定义
// init方法可以让用户先引入某个c文件进行初始化，初始化的那个c文件不可以再引入头文件
// 函数也可以变为全局都可以用的全局函数
extern JavaVM javaVM = NULL;
// extern 全局变量的设置

// 初始化结构体需要引入头文件, 不引入就再定义一遍
//extern
typedef struct node{

}Node;

extern Node node;
jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    javaVM = vm;

    return JNI_VERSION_1_6;
}
// 调用全局函数
extern void ex(){

}





