# Exploratory Black Box Test Util 
Project for IDT Winter 2017 Contest by [Swifty X](https://github.com/swiftyx)

## Software Requirements
- [Java SE Development Kit (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Git](https://git-scm.com/)
- Build tool(s). You should at least have one of the following
  - [Gradle 3.3](https://gradle.org/install)
  - [Apache Maven 3.x](http://maven.apache.org/)

## Compile, Build, and Execute
1. Open Command Prompt. That is PowerShell/cmd on Windows, or Terminal on macOS.
2. Download and navigate to the source code folder, using the following commands.
```
git clone https://github.com/swiftyx/com.idtus.contest.winter2017.framework.git
cd com.idtus.contest.winter2017.framework
```

### Build Using Gradle
```
gradle tasks
gradle build -x test
```
If you are using Windows, execute the compiled jar through the Batch file: 
```
idt.bat -jarToTestPath C:\\idt_contest\\jars\\TesterTypeCheck.jar -jacocoOutputPath C:\\idt_contest\\jacoco -jacocoAgentJarPath C:\\idt_contest\\jacoco\\lib\\jacocoagent.jar -bbTests 100 -timeGoal 1 -toolChain
```
Otherwise, execute the compiled jar through the bash file: 
```
sh idt.sh -jarToTestPath /idt_contest/jars/TesterTypeCheck.jar -jacocoOutputPath /idt_contest/jacoco -jacocoAgentJarPath /idt_contest/jacoco/lib/jacocoagent.jar -bbTests 100 -timeGoal 1 -toolChain
```

### Build Using Maven
```
mvn clean compile assembly:single
```
Execute the jar using java: 
```
java -jar target/com.idtus.contest.winter2017.framework-0.0.1-SNAPSHOT-jar-with-dependencies.jar -jarToTestPath C:\\idt_contest\\jars\\TesterTypeCheck.jar -jacocoOutputPath C:\\idt_contest\\jacoco -jacocoAgentJarPath C:\\idt_contest\\jacoco\\lib\\jacocoagent.jar -bbTests 100 -timeGoal 1
```

### Launch GUI
|Platform|Command|
|---:|:---|
|Windows|`idt-ui.bat`|
|Others|`sh idt-ui.sh`|

## Convenient Features
### Log Files
After you run a black box exploratory test, the results are automatically saved as `idt-yyyy-MM-ddTHH-mm-ss.out` in the source code folder.

### Define More Test Cases
You can define more test cases in a JSON file that will be executed by this test framework. The JSON file should be put on `$CLASSPATH` and have the same name as the jar under test. Please see the JSON files under `src/main/resources/json/` source directory for more information.
