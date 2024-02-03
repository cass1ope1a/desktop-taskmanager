# How to Run the Task Manager Application

**Prerequisites:**
- [Java](https://www.java.com/) installed on your machine.
- [JDK](https://www.oracle.com/java/technologies/downloads/) installed to compile and build the project.
- [Git](https://git-scm.com/) installed to clone the repository.

**Steps:**
1. **Clone the Repository:** <br>
   Open a terminal or command prompt and clone the Task Manager repository to your local machine.
    ```bash
    git clone git@github.com:cass1ope1a/desktop-taskmanager.git
    ```
2. **Navigate to the Task Manager project directory:** <br>
    ```bash
    cd desktop-taskmanager
    ```
3. **Build the Project using Maven:** <br>
    ```bash
    mvn clean install
    ```
4. **Run the Application:** <br>
    ```bash 
    java -jar target/desktop-taskmanager-1.0.jar
    ```