# cli-todoer
Utility to manage todo task from cli

# How to install

- **TO BE RELEASED**

## How to use

```
todo add “My first todo note in default section”
todo add “My first todo note in section 1” --sec 1 
todo list
todo list --note
todo list --section 1
todo list --section
todo list --section-only
todo delete
todo delete --section 1
todo delete --note 1
todo update --section 1 "I am updating section 1 title“
todo update --section 1 --note 1 "I am updating first note of section 1“
```
> Either you can use --section or  -s , --note or -n 

## How to build/test locally
Steps to be followed to build and test locally

1. Build the project
   ```
   ./gradlew clean build 
   ./gradlew shadowJar
   ```
2. Use the `./todo.sh` script to execute/test above subcommands
   ```
   ./todo.sh add "My first todo note in default section"
   ./todo.sh list
   ```