LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
TARGET_ARCH = arm
#APP_ABI := armeabi armeabi-v7a x86 mips mips-r2 mips-r2-sf
APP_ABI := armeabi armeabi-v7a x86 mips arm64-v8a
LOCAL_MODULE := ysh_lib
LOCAL_LDLIBS += -llog
LOCAL_SRC_FILES := com_subject_ysh_core_HttpService.cpp\
				   MD5.cpp
include $(BUILD_SHARED_LIBRARY)