cmake_minimum_required(VERSION 3.4.1)

add_library( hx-lib
             SHARED
             src/main/cpp/com_subject_ysh_core_HttpService.cpp
             src/main/cpp/MD5.cpp )

find_library( log-lib
              log )

target_link_libraries( hx-lib
                       ${log-lib} )