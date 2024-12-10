# liaotianapp
聊天APP

一． 项目介绍

      随着互联网技术的不断发展和普及，人们之间的即时联系变得越来越重要。在这个背景下，能够满足大家即时聊天需求的app像雨后春笋般冒了出来，非常典型的就是微信，qq等非常成功的聊天软件，本项目便仿照微信的设计，基于SQLite数据库，针对有些用户仅需日常轻量交流的需求，拟开发一款注重简洁、轻量的聊天app，专注于即时文字通讯功能，去除冗余的非必要功能，为用户提供安全、快速的沟通工具。
    
二． 项目功能

• 用户身份管理：

        1.支持用户进行根据已有的信息进行登录，也可以自己创建信息注册
        2.注册时，用户需填写基础信息（包括昵称、头像、账号。密码、性别）以供其他用户进行搜索识别，提升用户的个性化体验。
        3.支持用户修改个人信息、更改账号密码等，以便灵活管理自己的账户信息。
        4.支持用户退出登录至主界面后重新登录。
        
• 通讯录管理：

        1. 用户可通过搜索账号并添加好友
        2. 通讯录列表管理中打算利用昵称首字母进行排序，进行区分。
        3. 长按联系人界面可以选择是否删除该联系人
        
• 即时通讯功能：

        1. 实现实时文字消息的传递功能，保证消息传输的即时性和稳定性。
        2. 提供聊天时间记录功能，能够实时记录与好友之间最后一次发消息的时间，便于回溯沟通时间。
        
• 消息记录保存与检索：

           支持在本地消息记录的同步，方便用户能够查看历史聊天内容。
           
• 界面管理功能:

        1.进入界面时使用欢迎界面，同时支持点击退出；
        2.在主页面连续点击两下返回键可以退出应用。

三． 项目架构：

• 模块分层：
        ▪ MVC模式（Model-Viewer-Controller）
        
        ①Model层（bean）：包含应用的数据管理部分，主要通过SQLite数据库进行持久化存储，使用不同的类负责数据库的创建和升级，通过SQL查询加载联系人数据，并定义用户、联系人、聊天记录等的数据结构，作为应用的业务数据模型。
        
        ②View层（ui/activity，ui/fragment）：负责UI展示。定义了登录界面、搜索框、消息界面、搜索按钮、发送按钮、通讯录图标等UI元素，用户可以通过这些界面组件与应用进行交互。同时在将联系人列表数据适配到新类中，实现联系人列表的展示。
        
        ③Controller层（ui,util）：通过调用下层类中的方法实现应用逻辑和视图的交互，在用户点击、删除联系人等操作时，通过控制器方法对数据库进行相应的查询、删除操作，并根据结果动态更新视图。
        
• 面向事件的设计:

      代码在用事件监听器模式处理用户交互事件（包括点击、长按、搜索键入等），并通过这些事件触发控制器的响应操作，形成修改，发送，退出，跳转和界面变换等操作。
      
• 开发架构:

           前端开发：设计简洁、直观，方便的ui用户界面，并兼顾不同屏幕尺寸的适配效果。
           后端开发：实现消息传输、通讯录管理、用户信息管理，界面切换等核心功能，保障系统的稳定性。
        
四． 项目技术栈

        开发语言：Java
        开发环境：Android Studio
        数据库：SQlite
        架构模式：MVC
        UI框架：Android View
        
五． 核心库

插件依赖:

        •  classpath 'com.android.tools.build:gradle:7.4.2':
         Android Gradle 插件的依赖声明，指定了用于构建 Android 项目的 Gradle 插件版本为 7.4.2。是每个 Android 项目都必需的核心工具，提供了编译、打包、测试等必要的功能。
         
AndroidX 库 :

        •  implementation 'androidx.appcompat:appcompat:1.2.0':  
            提供了与旧版 Android 兼容的 Material Design 组件和主题。  
        •  implementation 'com.google.android.material:material:1.2.1': 
             包含了实现 Material Design 规范的各种 UI 组件。   
        •  implementation 'androidx.constraintlayout:constraintlayout:2.0.1':
             提供了一种灵活的布局管理器ConstraintLayout，允许创建复杂的界面而不需要嵌套多层视图。
测试库 :

           •  testImplementation 'junit:junit:4.+': 
               JUnit4测试框架，用于编写和运行单元测试。 
           •  androidTestImplementation 'androidx.test.ext:junit:1.1.2': 
               扩展了 JUnit 功能，提供了额外的工具来帮助测试 Android 应用程序。   
           •  androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0':
               一个行为驱动开发（BDD）的测试框架Espresso ，用于简化用户界面测试的编写
第三方库:

           •  implementation 'com.github.LuckSiege.PictureSelector:picture_library:v2.6.0':
               一个图片选择器库PictureSelector ，允许用户从相册或相机选择图片，并提供图片预览、裁剪等功能。
           •  implementation 'jp.wasabeef:glide-transformations:3.3.0':
               为 Glide 提供了额外的图像变换功能，如圆角、模糊等效果。
           •  implementation 'com.github.bumptech.glide:glide:4.11.0':
              一个快速高效的图片加载和缓存库Glide ，用于加载网络图片并进行缓存处理。
仓库配置 :

           • 阿里云 Maven 仓库： 
             使用了阿里云提供的 Maven 仓库镜像 (https://maven.aliyun.com/repository/central 和 https://maven.aliyun.com/nexus/content/groups/public/) 来加速国内用户的依赖下载速度。  
           • Google 官方 Maven 仓库：
             用于获取 Android SDK 和其他官方库。   
           • JCenter： 
             提供一些旧版本使用的库。
           • JitPack：
              https://www.jitpack.io：一个支持 GitHub 上托管的库的 Maven 仓库，允许直接从 GitHub 获取依赖项。


整体架构图：

![image](https://github.com/bcz0710/liaotianapp/blob/main/images/1.png)
      
六． 参考资料

git:

https://github.com/BATTERIA/BubbleWorld

https://github.com/xiuweikang/IM

https://github.com/chunyu-li/cyan

https://github.com/hussien89aa/KotlinUdemy

https://github.com/suihanll/Android_Chat

视频教程:

【手把手教你用Android Studio写一个APP-哔哩哔哩】 https://b23.tv/bMwzJp3

【Android基础课程-Glide使用详解-哔哩哔哩】 https://b23.tv/2LlscqQ

【面向对象设计模式重构：消除代码中难以维护的IF/ELSE-哔哩哔哩】 https://b23.tv/ltgAJWi

【【Android】聊天应用程序的开发-哔哩哔哩】 https://b23.tv/q84ZuxH

【【Android UI】从入门到项目实战（首页二级联动、灵动的锦鲤...）】https://www.bilibili.com/video/BV1P24y1o7eY?vd_source=31da57e89d118a9c93f95a4ced95163c

【课工场《APP测试》-Android测试环境搭建】https://www.bilibili.com/video/BV1dT4y1j7gH?vd_source=31da57e89d118a9c93f95a4ced95163c

七． 更新日志

系统整体uml图:
![images](https://github.com/bcz0710/liaotianapp/blob/main/images/2.png)

分工：

黄晟：对后端内容进行分析开发，进行第一版内容的分析和编写，并对其后端代码进行优化。

甄诚：对前端内容进行调整更改，优化对与用户的交互界面，同时对第一版前端进行优化。

黄晟：

10.27  配置环境时出现错误：
          ① Gradle 版本与项目所需的版本不兼容。更新了新的Gradle 到项目支持的版本。
          ② 对项目所需的依赖进行下载，更改镜像源，利用Android Studio中的"Sync Now"进行外部库的下载
          ③ 编译出现部分错误，并不兼容当前的 Android SDK 版本，调整部分代码以适应新的 Android API，修改了getSystemService() 中需要使用新的 API的部分，同时权限请求也产生了变化，对其进行更改
          
10.30   将旧的UI控件替换为新的组件，并把项目的 compileSdkVersion 更新为最新的 Android SDK 版本，应用可以勉强正常运行，完成了第一次配置

11.2     分析框架，定下软件结构，进行需求分析，编写开发方案，分析预期效果，并重新在 bean 目录下修改创建了数据模型类，用于存储新版应用中的数据对象。

11.6   利用各个xml布局文件，重新定义了界面布局，调整创建了ChatFragment、ContactsFragment、UserFragment  等底部界面

11.13   完善补充了UI用户界面，修改创建了各个 Activity，如main activity，login activity，chat activity等，并在 register activity中增加头像功能的时候出现问题，查找资料，发现从 Android 10（API 29）开始，Android 强制实施分区存储，于是改为新的 MediaStore API。

11.15 修改了适配器类，将数据模型绑定到 UI 组件；并修改添加了部分工具类MySqliteOpenHelper，Sputils，处理一些通用的逻辑，实现了对数据库的便捷查找和高亮显示。

11.19  在各个 Activity 和 Fragment 中完善业务逻辑，包括登录、注册、聊天等，完成第一版。

11.21  对Contact类进行重写优化，增加了删除联系人的功能，重写了 equals 和 hashCode，确保在集合中进行高效查找和比较，将字段设置为了 final，仅提供必要的构造函数，并加入serializble接口，使对象可被序列化，方便进行对象的持久化存储。

11.24  对PersonChat类进行重写优化，合并了两个构造函数，减少冗余代码，提供默认值传递功能；同时使用了标准的 getter 和 setter 方法，确保了前后代码的一致性。同Contact一样，重写了 equals 和 hashCode 方法，保证类在集合中具有一致的比较行为。
对User类进行重写优化，加入了serializble接口，使得对象能够被序列化，方便进行对象的持久化存储。并更新了代码的结构，重新规范了命名。
           
11.26  对Glide Engineer进行重构，将重复逻辑抽取成通用方法，并拆分不同的加载逻辑，让每个方法只负责自己的部分，优化了代码的可读性和可维护性。并增加了长图适配与GIF配置图功能。

11.29 增加了LetterView类，将颜色和大小设置提取到常量，实现通讯录中对字母的点击高亮处理。

12.3   增加了RecyclerScollerView类，在触摸事件中加入了一定的滑动阈值，增强了滑动操作的体验和控制能力。

12.6   对TransPinYi类进行优化，优化了查找算法，使用二分法进行查找，提高了效率。
          增加了双击返回退出效果。


甄诚

11.15
AddActivity（重构）
         
1.代码结构优化

        将数据库操作放在 loadData() 中：原代码中，数据库操作是在异步任务中进行的，而重构后代码将这些操作放回到主线程中直接执行。在重构后代码中，loadData() 负责查询数据库并返回结果，避免了使用 AsyncTask，使得代码更加简洁。掉了多余的成员变量：重构后代码中移除了 userList 作为类成员变量，直接在 loadData() 中定义并使用局部变量。这样做简化了代码，避免了不必要的类级别的数据存储。
         
2.UI 事件处理的优化

          在原代码中，setViewListener() 方法中有两个地方处理了搜索功能：一个是搜索图标 ivSearch 的点击事件，另一个是 EditText 中的软键盘搜索按钮（IME_ACTION_SEARCH）的事件。重构后代码保留了这两个事件监听，但在实现上稍作优化，通过 loadData() 方法统一处理搜索操作。
         
3.数据库操作的优化

          查询操作的优化：在原代码中，LoadDataTask 异步加载数据，而重构后代码直接在主线程的 loadData() 方法中进行查询，去掉了 AsyncTask。这使得代码更加简洁，去掉了不必要的线程切换操作，但这种方式可能会影响性能，特别是数据量较大的时候。直接使用 SQLiteDatabase 查询：在重构后代码中，直接通过 SQLiteDatabase 对象执行查询语句，代码更加简洁易懂。例如，在loadData() 中直接执行 SQL 查询。这个改动使得代码的可读性有所提升，避免了不必要的异步处理。
          
4.用户交互逻辑优化

          确认添加联系人对话框：在重构后代码中，ItemClick 的事件处理逻辑与原代码相似，都是弹出一个确认框询问用户是否要添加该联系人。重构后，代码在弹出对话框时简化了逻辑，没有过多的嵌套，使得代码的执行流程更清晰。这种方式避免了多层嵌套和重复代码。
5.代码的可维护性

          去除冗余代码：在重构后代码中，移除了原代码中冗余的变量和方法，使得每个方法职责更加单一。比如，userList 在原代码是类级别的成员变量，而在重构后代码中，它被简化成了一个局部变量，避免了不必要的内存占用和数据传递。同样，AsyncTask 也被移除，减少了线程管理的复杂度，这使得代码更简洁。

11.19
LoginActivity（重构）
 
1.代码结构优化：

          在原代码中，用户登录信息（如用户名、密码等）是通过网络请求处理的（通过 UserPresenter 和 UserView），而在重构后代码中，所有的数据存储与查询都通过本地的 SQLite 数据库进行处理。重构后代码通过直接操作数据库（SQLiteDatabase 和 Cursor）来查询用户信息，并验证密码，避免了与服务器的交互，使得登录过程更加简洁高效。
 
2.核心功能调整与改进：

          原代码通过网络请求向服务器提交用户名和密码进行验证，而重构后代码直接在本地数据库中查询存储的密码并进行比对。通过 SQL 查询语句，重构后代码判断账号是否存在并进行密码验证。重构后代码使用 SharedPreferences（通过 SPUtils 封装）来保存用户的 ID，而原代码通过 SharedHelper 管理登录状态，也使用了 SharedPreferences 来存储登录状态。通过 SPUtils 进行存储是为了简化代码，便于后续的读取和使用。
 
3.UI 交互设计优化：

          在重构后代码中，注册页面通过 TextView 的点击事件实现，保持了简单的实现方式。相较于原代码，重构后代码没有使用 ButterKnife 进行视图注入，更多依赖手动绑定，减少了外部库的引入。
         
4.异常处理优化：

          重构后代码在用户输入不完整（如账号或密码为空）时，立即给出提示，确保用户输入完整的登录信息，避免无意义的数据库查询。并且，若账号不存在或密码错误时，给予明确的错误提示，改进了用户体验。
         
         
RegisterActivity（重构）
 
1.头像功能的改进：

          原代码中没有涉及头像上传功能，重构后代码通过 ImageView 提供头像选择功能，点击头像可以选择本地图片作为用户头像。重构后代码使用 PictureSelector 库来处理头像选择，支持图片压缩、裁剪等操作，提升了用户体验。
         
2，UI 和交互优化：

          重构后代码中增加了性别选择（通过 RadioGroup 实现），使得注册表单更符合实际需求。代码的布局更加清晰，按功能划分的结构让每个模块都更为独立和易于理解。例如，点击登录跳转的逻辑清晰分离。
 
3.表单验证和用户反馈：

          重构后代码在用户注册表单完善了验证功能，除了验证用户名和密码之外，还增加了头像上传的校验，并检查两次密码是否一致。验证失败时会给出明确的提示，如头像未上传、密码不一致等错误信息，让用户更容易理解并修正输入。
 
4.拼音首字母生成：

          重构后代码根据昵称生成拼音首字母，作为联系人排序的标识，增加了用户信息的可管理性。
         
5.数据库操作：

          原代码和重构后代码都采用了 SQLite 来存储用户信息，但重构后代码在查询用户是否已存在时，使用了更为简洁的 rawQuery，并且增加了针对账号存在与否的提示。
        
 11.24
 OpenActivity（自写）
 
这段代码是实现一个欢迎界面。代码涉及到用户初始化数据、检查用户是否第一次启动应用、根据用户登录状态跳转到不同的页面等功能。以下是代码的设计思路：
         
1. 初始化和布局设置

          通过 setContentView 设置当前 Activity 使用的布局文件 activity_open.xml。初始化了一个 MySqliteOpenHelper 对象 helper，用于操作数据库。获取了一个按钮 in，该按钮用于跳过欢迎页并手动进入主页面或登录页面。
         
2. 用户数据加载

          从 SharedPreferences 中获取 userId 和 userType，用于判断用户是否已登录及其类型。
 
3. 倒计时和跳过功能

          创建了一个 CountDownTimer，用于实现一个 5 秒倒计时，每秒更新一次 in 按钮上的文本显示倒计时秒数。当倒计时结束时，调用 finishView() 方法，自动跳转到登录或主页面。用户也可以点击按钮跳过倒计时，立即跳转。
         
4. 初始化本地数据（第一次启动）

          判断是否是用户第一次启动应用。若是第一次启动（通过 SPUtils 获取 IF_FIRST 标志），则进行本地数据初始化。读取 assets 目录下的 db.json 文件，解析 JSON 数据并将其插入 SQLite 数据库。具体操作为：从 user 数组中获取用户信息（account, password, name, sex, photo），将其存储到 SQLite 数据库中的 user 表里。同时生成用户的首字母（拼音首字母的大写）作为查询字母，用于可能的用户列表展示。
         
5. 数据插入（转换拼音并生成首字母）

          使用工具类 Trans2PinYin 将用户的名字转换为拼音，并取其首字母作为 letter。如果首字母不在字母数组中（strChars），则将其设置为 #。这样做的目的是确保首字母符合预设的字母范围，避免用户名字开头为非字母字符时出错。
         
6. 跳转逻辑

           根据 userId 是否大于 0 来判断用户是否已登录。如果已登录，则跳转到 MainActivity。如果未登录，则跳转到 LoginActivity。最后，通过 startActivity(intent) 启动相应的 Activity，并调用 finish() 关闭当前 OpenActivity。
 
7. 错误处理

          在文件读取和 JSON 解析过程中，使用了异常处理机制，捕获并打印 IOException 和 JSONException 错误，避免应用崩溃。

11.28
UserFragment（补充）
        
由于准备新增了关于界面，个人信息编辑页面，重置密码界面，在UserFragment部分就要补充。
         
个人信息：用户点击个人信息部分 时，跳转到 PersonActivity 页面，可以查看和编辑个人信息。

重置密码：用户点击账号安全部分 时，跳转到 PasswordActivity 页面进行密码修改等操作。

关于：点击关于部分about，跳转到 AboutActivity 页面，通常用于显示应用信息。
        
具体操作如下：

创建 Intent 对象：指定目标 Activity。

传递数据：如果需要传递数据，通过 putExtra() 方法传递必要的参数（例如用户 ID）。

启动目标 Activity：调用 startActivity(intent) 启动目标页面。
        
11.29
PasswordActivity （自写）
 
这部分实现了一个Android 应用中的重置密码功能。下面是具体功能以及代码思路：

1.展示界面

          提供一个界面，允许用户输入新的密码。用户可以看到一个EditText 控件，用于输入新密码。
 
2.密码验证

          密码不能为空：如果用户没有输入新密码，弹出提示(Toast) 提示用户“新密码为空”。
    
          密码格式验证：使用自定义工具方法Tools.isPassword() 验证密码格式，要求密码是 8-16 位的字母和数字组合。若格式不符合要求，提示用户“密码格式错误，请输入8-16位字母数字组成”。
 
3.更新密码
 
          通过SQLite 数据库更新当前用户的密码。用户的 ID 通过 SharedPreferences 获取，并用该 ID定位到用户记录进行更新：执行 SQL 语句：UPDATE user SET password = ? WHERE id = ?。更新成功后，弹出提示“更新成功”，然后关闭当前页面。
 
4.用户身份识别
    
          从SharedPreferences 中获取当前用户的 ID (SPUtils.get(PasswordActivity.this, SPUtils.USER_ID, 0)) 以确定哪个用户需要更新密码。
 
5.返回操作
    
          提供一个返回按钮，用户点击后直接关闭当前页面（通过finish()）。
        
12.7
 PersonActivity（自写）
        
 这段代码实现了一个个人信息编辑的功能，允许用户查看和修改其昵称及性别信息。它主要包括通过数据库查询用户信息并展示在界面上，提供修改后的数据保存功能，和一个简单的返回功能。以下是设计思路：
         
1.布局和视图初始化

          在 onCreate 方法中，首先通过 setContentView 设置界面布局 activity_person.xml。获取界面中各个 UI 控件的引用：TextView 用于显示账户信息，EditText 用于编辑昵称，RadioGroup 和 RadioButton 用于选择性别，Button 用于保存修改后的信息。
         
2.获取用户信息并显示

          从 Intent 中获取传入的 userId，然后通过该 userId 从数据库中查询对应的用户信息。使用 SQLiteDatabase 和 rawQuery 执行查询，获取用户的相关信息（如账户、密码、昵称、性别、电话等）。将查询到的账户信息 (dbAccount) 显示在 TextView (tvAccount) 中。将昵称 (dbName) 显示在 EditText (etNickName) 中，允许用户修改。根据查询到的性别 (dbSex)，在性别 RadioGroup 中选中相应的性别（男或女）。
         
3.保存修改的用户信息

          当用户点击保存按钮 (btnSave) 时，触发点击事件处理逻辑。获取用户输入的昵称 (etNickName.getText().toString()) 并进行空值检查，确保昵称不能为空。若为空，弹出 Toast 提示用户输入昵称。根据 RadioGroup 中选中的 RadioButton 获取性别，若选中的是男，设置性别为 "男"，否则设置为 "女"。使用 SQLiteDatabase 的 execSQL 方法执行 UPDATE SQL 语句，将修改后的昵称和性别保存到数据库。提示用户更新成功 (Toast)，并关闭数据库连接。最后调用 finish() 结束当前 Activity，返回到前一个页面。
         
4.返回功能

          提供一个返回按钮，用户点击时会直接关闭当前页面并返回上一页面。
        
12.8
AboutActivity （自写）
 
这部分很简单，只涉及基本的界面展示和一个简单的返回功能:
 
1.继承 AppCompatActivity
        
          代码中的 AboutActivity 继承自 AppCompatActivity。使用 AppCompatActivity 可以利用 Android 支持库提供的兼容性特性，方便进行 UI 设计和组件的使用，尤其是在较旧的 Android 设备上。
 
2.onCreate() 方法
        
          onCreate(Bundle savedInstanceState) 是 Activity 生命周期的一部分，应用启动时会调用该方法。setContentView(R.layout.activity_about)：在 onCreate 方法中，使用 setContentView 来加载界面布局文件 activity_about.xml。这意味着该页面显示的是该 XML 布局文件中定义的视图。
 
3.返回按钮 (back(View view))

          设计方法 back(View view) 处理用户点击返回按钮时的操作。点击返回按钮时，调用 finish() 方法销毁当前 Activity，并返回到上一个页面。
        

八． 测试

本次主要用到了InstrumentedTest和UnitTest来测试代码运行

仪器化测试（Instrumented Test）是运行在 Android 设备或模拟器上的测试。它们通常用于测试与 Android 系统相关的功能，例如 UI 交互、数据库操作、网络请求等。
特点

• 运行环境：需要实际的 Android 环境（如模拟器或真实设备）。

• 依赖 Android 框架：可以访问 Android 的系统资源和 API。

• 执行时间：一般较长，因为需要设备或模拟器的支持。

• 典型用途：
    1.测试 UI 行为和用户交互。
    2.测试数据库或文件存储操作。
    3.测试 Android 系统组件（例如 Activity、Service 等）。
    
单元测试（Unit Test）用于在本地 JVM（Java Virtual Machine）中运行，通常测试独立的业务逻辑，而无需依赖 Android 框架。
特点

• 运行环境：仅需 JVM 环境（本地运行）。

• 不依赖 Android 框架：无法使用 Android 的特定 API。

• 执行时间：通常较快，因为无需启动设备或模拟器。

• 典型用途：
    1.测试纯 Java 方法的逻辑正确性。
    2.测试算法、数据结构、工具类等。
    
部分测试代码及结果：
![image](https://github.com/bcz0710/liaotianapp/blob/main/images/3.png)
![image](https://github.com/bcz0710/liaotianapp/blob/main/images/4.png)
![image](https://github.com/bcz0710/liaotianapp/blob/main/images/5.png)
![image](https://github.com/bcz0710/liaotianapp/blob/main/images/6.png)

九、总结

整体优化总结

本次代码重构主要实现了以下优化：

1.减少重复代码，减少不必要的构造函数，简化函数判定条件，使代码更加简洁，减少计算量。

2.明确代码逻辑，拆分一些比较冗杂的加载逻辑，让每个方法只负责自己的部分。尽量避免过多的嵌套逻辑。

3.通过添加适当的注释和使用常量和易懂的变量名，使得代码易于理解和维护。

4.优化数据库查询，确保资源的正确释放，避免内存泄漏。

5.增加了许多异常处理，使异常信息更为明确，方便调试和维护。

6.新增一些actrivity,优化用户操作体验,例如:增加重置密码,增加个人信息可改变,增加开始欢迎界面,让软件运行更流畅，增加关于界面，显示版本更新。

量化对比:

![image](https://github.com/bcz0710/liaotianapp/blob/main/images/7.png)
