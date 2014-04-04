#local_path 代表的是当前android.mk文件所在的路径
#$() 代表的是一个函数
LOCAL_PATH := $(call my-dir)

#CLEAR_VARS 清空变量
#清空所有以LOCAL_ 开头的变量里面的内容 (不会清空LOCAL_PATH里面的数据)
include $(CLEAR_VARS)

#定义编译后的C代码库的名称，可显示地在名字前面加lib，但不能在后面加“.so”，否则报错
#即便定义为libnorika，也还是调用norika这个库
LOCAL_MODULE    := norika
#定义makefile 编译的源文件
#依赖的头文件 是不需要指定的.
LOCAL_SRC_FILES := norika.cpp
#由于这里使用到了log.h这个头文件，那么在编译的时候要把log.h对应的二进制代码库（liblog.so）引入到交叉编译的系统里面
#文件来源于{ndk_home}/platforms/android-19/arch-arm/usr/lib
#指定需要加载一些别的什么库. 
LOCAL_LDLIBS  += -L$(SYSROOT)/usr/lib -llog

#生成一个动态的代码库
include $(BUILD_SHARED_LIBRARY)
# include $(BUILD_STATIC_LIBRARY); 生成一个静态的代码库
# 静态代码库的作用 主要就是用来提供一些库函数 编译的时候 可能需要用到静态代码库.
