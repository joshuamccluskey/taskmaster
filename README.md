# Task Master
Android task management application. Manage all your tasks from one app.

## Implementation
- You can run in app from app/
- If needed app-debug.apk file in root taskmaster/
- [app-debug.apk](app-debug.apk)

# Daily Log 04.11.2022

### Overview

Refactor fix bugs on outside intent image and lat lon location

### Updates
- Add Lat lon location of the user and add it to each task
- Add analytics to activity
- Add text speech to a read task




### Features
- As a user I want to record my lat lon on a task
- As a user I want to upload images from an outside source

### Work Time
- TBD

# Daily Log 04.10.2022

### Overview

Refactor fix bugs on outside intent image and lat lon location

### Updates
- Add Lat lon location of the user and add it to each task
- Feature to upload outside files to each task
![imageIntentLab38](screenshots/imageIntentLab38.png)
![imageIntent2Lab38](screenshots/imageIntent2Lab38.png)
![addTaskLab38](screenshots/addTaskLab38.png)
![imageEditLab38](screenshots/imageEditLab38.png)



### Features
- As a user I want to record my lat lon on a task
- As a user I want to upload images from an outside source

### Work Time
- 6 Hours

# Daily Log 04.06.2022

### Overview

Feature to upload information or outside files

### Updates
- Feature to upload outside files to each task


### Features
- As a user I want to record my lat lon on a task

### Work Time
- 1 Hour

# Daily Log 04.05.2022

### Overview

Setup S3 to allow user to store images for each task on the app

### Updates
- Allows user to upload image files to each task

- ![imageAddLab37](screenshots/imageAddLab37.png)
- ![imageViewLab37](screenshots/imageViewLab37.png)
- ![s3Savedlab37](screenshots/s3Savedlab37.png)


### Features
- As a user I want to upload files to each task

### Work Time
- Work Time 8 Hours



# Daily Log 04.04.2022

### Overview

Setup log in and security for TaskMaster

### Updates
- Build Log in Log out flow

![loginLab36](screenshots/loginLab36.png)
![signupLab36](screenshots/signupLab36.png)
![verifyEmailLab36](screenshots/verifyEmailLab36.png)
![verificationLab36](screenshots/verificationLab36.png)
![loginAuthLab36](screenshots/loginAuthLab36.png)
![loginSuccessLab36](screenshots/loginSuccessLab36.png)
![awsUserAccountLab36](screenshots/awsUserAccountLab36.png)

### Features
- As a user I want to login and protect my account and task information

### Work Time
- 7 hours


# Daily Log 03.31.2022

### Overview

Polish app up with all features complete and deploy app to the Google Play Store

### Updates
- Publish to the Google Play Store
- Publish complete under production review
![googlePlayStoreLab34](screenshots/googlePlayStoreLab34.png)
  
- Team Conditional Render
![mainActivityLab34](screenshots/mainActivityLab34.png)
![addTaskLab34](screenshots/addTaskLab34.png)
![teamChangeLab3](screenshots/teamChangeLab34.png)
![teamTaskLab34](screenshots/teamTaskLab34.png)

- Update settings to choose team and conditionally render tasks for that team
  - *Bug on Setting adding team setting*

### Features
- As a user I want to be able to update task information
- As a user I want to be able to download the app from Google Play



### Espresso Tests
- taskNameTest assertion test that checks task name equals textView on Task Details page
- uiElementsMyTaskActivityTest assertion test checking if critical elements exist on page rebuilt for reliability
- usernameTest assertion test checking textview of username matches username entered on setting page

### Work Time
- 6 Hours

- 4 hours 04.04.2022  Debugged app crash on settings page in order to publish on Google Play

# Daily Log 03.30.2022

### Overview

App will use teams. These teams will be assigned tasks specific tasks.

### Updates
- Update Add Task Form with team using a radio button

  ![teamAddTaskLab33](screenshots/teamAddTaskLab33.png)
  ![teamSelectedLab33](screenshots/teamSelectedLab33.png)
  ![teamsDynamoLab33](screenshots/teamsDynamoLab33.png)
  ![MyTasksLab33](screenshots/MyTasksLab33.png)
  ![MyTasksLab33](screenshots/tasksDynamoLab33.png)
  
- Update settings to choose team and conditionally render tasks for that team
  - *Bug on Setting adding team setting*
  
### Features
- As a user I want an app that lets me assign taks to a team
- As a user I want choose my team and see the taks assigned ot the team


### Espresso Tests
- taskNameTest assertion test that checks task name equals textView on Task Details page
- uiElementsMyTaskActivityTest assertion test checking if critical elements exist on page rebuilt for reliability
- usernameTest assertion test checking textview of username matches username entered on setting page

### Work Time
- Work time: 5 Hours

# Daily Log 03.29.2022

### Overview

Integrate AWS for Cloud Data Storage and for scalable backend

### Updates
- Style
- AWS - DynamoDB
- Overhauled Backend took out Room DB and Replaced with all AWS
![MyTaskActivityLab32](screenshots/MyTaskActivityLab32.png)
- Add Task Adds Task to DynamoDB
  ![AddTaskActivtyLab32](screenshots/AddTaskActivtyLab32.png)
- onResume Pulls tasks from DynamoDB
  ![MyTaskActivtyUpdatedResumeLab32](screenshots/MyTaskActivtyUpdatedResumeLab32.png)
### Features
- As a user I want an app that uses cloud services on the backend using DynamoDB
- As a user I want an app that adds a task to DynamoDB
- As a user I want an app that shows my current list of tasks automatically
- As a user I want a clean design and style and smooth and intuitive user experience

### Espresso Tests
- taskNameTest assertion test that checks task name equals textView on Task Details page
- uiElementsMyTaskActivityTest assertion test checking if critical elements exist on page rebuilt for reliability
- usernameTest assertion test checking textview of username matches username entered on setting page

### Work Time
- Work time: 7 Hours

### Links
[Adobe Color](https://color.adobe.com/search?q=light%20blue)
[Gradient Color](https://evangelidis.medium.com/android-how-to-set-gradient-color-as-background-7812c4cf06ec)


# Daily Log 03.28.2022

### Overview

Polish up app and add test main components with Espresso. Make sure to polish user experience

### Updates
- Auto update recyclerView from Room Database and Auto rredirect to MyTasks from Add Task when submitted.
  ![myTasksAutoUpdateAddTaskLab31](screenshots/myTasksAutoUpdateAddTaskLab31.png)
  
- When Task is tapped, the Deatils page appears and the Title is set and description
  - Note:  Debug fix when using putExtra approach Enums are an object and require toString
  ![TaskDetail](screenshots/taskDetailLab31.png)

### Features
- As a user I want a polished site that functions without error and is automated with good user experience.
- Espresso tests created for all critical elements

### Espresso Tests
- taskNameTest assertion test that checks task name equals textView on Task Details page
- uiElementsMyTaskActivityTest assertion test checking if critical elements exist on page
- usernameTest assertion test checking textview of username matches username entered on setting page

### Work Time
-4 hours


# Daily Log 03.24.2022

### Overview

Adding RecyclerViews in order to display a list of data on the homepage.

### Updates
- Set up a Room database and save Tasks in Lcoal Database
  ![RoomData](screenshots/roomDataLab29.png)
- Update My Tasks page to display from local database your task in recyclerView
  ![recycleViewRoomDataLab29](screenshots/recycleViewRoomDataLab29.png)
  ![AddTaskLabSpinner29](screenshots/AddTaskLabSpinner29.png)
  ![recycleViewNewTaskLab29](screenshots/recycleViewNewTaskLab29.png)
  
- When Task is tapped, the Deatils page appears and the Title is set and description
  *Still needs description and status to appear*
  ![TaskDetail](screenshots/TaskDetailLab29.png)

### Features
- Add Task Model Class: As a user I want to create a task and save it top the  local database
- Add RecycleViewer to Display List of Tasks: As a user I want to see my list of task on the My Tasks Page in local database
- Add Tap Task Item: As a user I want to be able to tap my tasks on the homepage to see specific task details like title and description.

### Tests
- Espresso test goPagesTest Test all "goToPages" intents on buttons and recycler view taps

### Work Time
-6 hours


# Daily Log 03.23.2022

## Refactor 03.24.2022 Fixed Bug updating Task Detail Title from RecyclerView items

### Overview

Adding RecyclerViews in order to display a list of data on the homepage.

### Updates
- Add recycler views to My Tasks page
  ![RecyclerView](screenshots/recycleViewLab28.png)
  ![RecyclerViewScroll](screenshots/recycleViewScrollLab28.png)
  
- Add tap feature to list item to take you to task deatils page
  ![RecycleVeiwTapTaskDetailTitle](screenshots/recycleViewTaskDetailTitleLab28.png)
  ![RecycleVeiwTapTaskDetailTitle](screenshots/recycleViewTaskDetailTitleGroceriesLab28.png)
  ![RecycleVeiwTapTaskDetailTitle](screenshots/recycleViewTaskDetailTitlePuppyLab28.png)
### Features
- Add Task Model Class: As a user I want to create a taks with a title, body, and state: “new”, “assigned”, “in progress”, or “complete”.
- Add RecycleViewer to Display List of Tasks: As a user I want to see my list of task on the My Tasks Page.
- Add Tap Task Item: As a user I want to be able to tap my tasks on the homepage to see specific task details. 

### Tests
- Espresso test goPagesTest Test all "goToPages" intents on buttons and recycler view taps

### Work Time
- 6 Hours
- 30 minutes Fixed Bug; Updated README with more screenshots; Built new APK

# Daily Log 03.22.2022

### Overview

Adding the SharedPreferences and intents for data to be added to our Task Master app.

### Updates
- Settings built
  ![Settings Page](screenshots/settingsLab27.png)
- My Tasks with username and task buttons built
  ![Before Username](screenshots/hompageBeforeUSernameLab27.png)
  ![After Username](screenshots/homepageUsernameLab27.png)
- Task Details built
  ![Do Taxes](screenshots/doTaxesLab27.png)
  ![Groceries](screenshots/groceriesLab27.png)
  ![Dog Food](screenshots/dogFoodLab27.png)

### Features
- Add Task Detail Page: As a user I want to be able to see details of individual tasks on the homepage with a buttons or link to see deatils
- Add Setting Page: As a user I want to be able have a settings page where I update my username.
- Add Task Title Button: As a user I want to be able to click my tasks on the homepage to see specific task details. The
Title should upodate on the task detail page.
  
### Tests
- [AllButtonTasksTaskDetailsMyTasksActivityTest](app/src/androidTest/java/com/joshuamccluskey/taskmaster/AllButtonTasksTaskDetailsMyTasksActivityTest.java)
- [DoTaxesTest](app/src/androidTest/java/com/joshuamccluskey/taskmaster/DoTaxesTest.java)

### Work Time

- 3.5 Hours

# Daily Log 03.21.2022

### Overview

Wireframe build of the TaskMaster Android app. My Tasks page, Add Tasks page, All Tasks page built
  
### Updates

#### My Tasks page built
![My Tasks Page](screenshots/myTasksLab26.png) 
#### Add Task page built
![Add Task Page](screenshots/addTaskLab26.png)
#### All Tasks page built
![My Tasks Page](screenshots/myTasksLab26.png)

### Features
- Add a homepage: As a user I want to be able to see my tasks on the homepage with buttons to add and see all tasks
- Add a task: As a user I want to be able to add a task and see its been submitted.
- Add Task Title and Description: As a user I want to be able to add the task title and description.
- Task page only contains an image no function
  
### Work Time

- 5 hours
- 2 hours Debugging and adding visibility of submitted message onClick Add Task Button
