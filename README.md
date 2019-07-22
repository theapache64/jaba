[![IMAGE](https://raw.githubusercontent.com/theapache64/jaba/master/youtube_banner.jpg)](https://www.youtube.com/watch?v=VZ8KAkvw9ck)


# Jaba

A simple cli tool to convert your stock android project structure to MVVM architectural pattern. 
It also includes templates for splash and login screen. Basically, jaba is a time saver. :)

## Download

Clone or download this repo and set an alias to the `jaba.jar` file

```
alias jaba='java -jar /path/to/jaba/jaba.jar'
```

## Usage

Simply run `jaba` in an android studio project, and you'll be prompted with an interactive shell.

```

Project : MyAwesomeProject
Package : com.theapache64.myawesomeproject

Choose architecture
1) MVVM
2) MVP
Response :1
Do you need google fonts? (y/N): y
Response : yes
Do you need network module ? (y/N): y
Response : yes
Enter base url : (empty to use default jaba api): http://myapi.com/api/v1/
Do you need splash screen? (y/N): y
Response : yes
Do you need login screen? (y/N): y
Response : yes
⏳ Creating dirs...
✔️ Done
⏳ Modifying app.gradle
✔️ Done
⏳ Fixing XML style issue...
✔️ Done
⏳ Creating App.kt ...
✔️ Done
Modifying manifest file...
✔️ Done
⏳ Creating MainViewModel.kt ...
✔️ Done
⏳ Modifying MainActivity.kt ...
✔️ Done
⏳ Adding data binding to main layout file...
✔️ Done
⏳ Updating content_main.xml to support data binding
✔️ Done
⏳ Creating dagger activity builder...
✔️ Done
⏳ Creating dagger AppComponent.kt ...
✔️ Done
⏳ Creating network module...
✔️ Done
⏳ Creating ApiInterface.kt ...
✔️ Done
⏳ Creating LogInRequest.kt ...
✔️ Done
⏳ Creating LogInResponse.kt ...
✔️ Done
⏳ Creating UserPrefRepository.kt ...
✔️ Done
⏳ Creating LogInActivity.kt ...
✔️ Done
⏳ Creating LogInViewModel.kt ...
✔️ Done
⏳ Creating LogInClickHandler.kt ...
✔️ Done
⏳ Creating AuthRepository.kt ...
✔️ Done
⏳ Creating login layout...
✔️ Done
⏳ Creating login related icons
✔️ Done
⏳ Adding login strings to strings.xml
✔️ Done
⏳ Modifying menu_main.xml file
✔️ Done
⏳ Creating dagger AppModule.kt ...
✔️ Done
⏳ Creating dagger ViewModelModule.kt ...
✔️ Done
⏳ Creating SplashViewModel.kt
✔️ Done
⏳ Creating SplashActivity.kt ...
✔️ Done
⏳ Modifying styles.xml to support splash theme
✔️ Done
⏳ Creating splash_bg.xml ...
✔️ Done
⏳ Creating ids.xml ...
✔️ Done
⏳ Creating logo icon...
✔️ Done
⏳ Adding color constants to colors.xml
✔️ Done
⏳ Finishing project setup...
✔️ Done
```

## Activity Support

Once you created a new activity, you'll want to create `ViewModel`, `Handler` and integrate them with the `DataBinding`, `Activity` and with `Dagger`. To solve this mundane task, you may use the `pas` flag to **Provide Activity Support**

Syntax

```
jaba -pas COMPONENT_NAME
```

For example,If you want activity support for `SomeActivity`, you may use below code

```
jaba -pas SomeActivity
```

Then it'll do the following

### Creation

- Create `ViewModel` named `SomeViewModel` with `dagger` injection
- Create `Handler` named `SomeHandler`

### Integration

- Integrate `SomeViewModel` with `SomeActivity` with  `dagger-viewmodel-factory`
- Implement `SomeHandler` in `SomeActivity`
- Integrate `SomeViewModel` and `SomeHandler` with `activity_some.xml` (using data binding)

### Finalization

- Add `SomeActivity` builder in `ActivityBuilderModule`

- Add `SomeViewModel` in `ViewModelModule`


## Known Issues

**Code Alignment**

Once the code generation has been done, some files will have some alignment issues.
To fix this, you may do `Code Refactor` 

- Click on the `Project` panel
- Click on `app`
- Press `Control + Alt + L` (linux)
- Check `Optimize Imports, Rearrange entries and Cleanup code`
- Click 'Run'

This will solve all alignment issues as well as optimization of imports.

## Project Structure

**Before**

```
.
├── MainActivity.kt
```

**After**
```
.
├── App.kt
├── data
│   ├── local
│   ├── remote
│   │   ├── ApiInterface.kt
│   │   └── login
│   │       ├── LogInRequest.kt
│   │       └── LogInResponse.kt
│   └── repositories
│       ├── AuthRepository.kt
│       └── UserPrefRepository.kt
├── di
│   ├── components
│   │   └── AppComponent.kt
│   └── modules
│       ├── ActivitiesBuilderModule.kt
│       ├── AppModule.kt
│       ├── NetworkModule.kt
│       └── ViewModelModule.kt
├── models
├── ui
│   └── activities
│       ├── login
│       │   ├── LogInActivity.kt
│       │   ├── LogInClickHandler.kt
│       │   └── LogInViewModel.kt
│       ├── main
│       │   ├── MainActivity.kt
│       │   └── MainViewModel.kt
│       └── splash
│           ├── SplashActivity.kt
│           └── SplashViewModel.kt
└── utils

```
