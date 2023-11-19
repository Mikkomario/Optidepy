# Optidepy
A command line application suitable for performing or preparing software deployments - or backups

## Using Optidepy
In this section, we'll quickly cover how to run and use Optidepy for deployment and/or backups.

### Starting the software
In order to start Optidepy, use the command line + `cd` command to navigate to the directory where Optidepy.jar resides. 
Once there, use `java -jar Optidepy.jar` command.

### Creating a project
Before you can deploy your project, you need to register it in Optidepy. For this, use the 
`add <project> <input> <output>` command.

You will need to specify the following parameters:
1. **Project**: The name you want to give to your project. Use a unique name for each of your projects.
2. **Input**: The file path that is common to **all** the files you want to deploy. 
  Typically, this is your project workspace path. You may specify the path as an absolute path (recommended) 
  or relative to your current directory.
3. **Output**: Path to the directory where your project files will be deployed. Multiple version and build directories 
  will be added under this path.

You may then specify 1-n specific paths under `<input>` that should get deployed, plus their relative paths under 
each output directory. For example, if your `<input>` is defined as `C:\Users\Me\Projects\MyProject`, you may specify 
that `out\artifacts\MyArtifact` (under MyProject) goes to `.`, which would correspond to the root directory 
in each deployment. You can choose to deploy whole directories or individual files.

Finally, the software will ask you two more default settings that shall apply to this project:
1. "Do you want to collect changed files to **separate build directories**?"
2. "Should **automatic deletion** of non-source files be enabled?"

If separate build directories are enabled, each changed file gets deployed to two locations:
1. The "full" version of your project, which contains the latest versions of all your project files, and 
2. A build directory which is created to represent that deployment, specifically

For example, when preparing a server deployment, you likely want to use separate build directories because you can 
then simply move the contents of the build directory to the server, without moving the unmodified files. 
However, if you're performing a backup, for example, you probably don't want separate build directories, as they 
take additional space on your hard drive.

Automatic file deletion, on the other hand, is suitable for all projects except those where you manually add or modify 
files after deployment. For example, if you're deploying an update for a local test environment, where the software 
generates additional files, these files would get automatically deleted upon every deployment, if automatic 
file-deletion is used. This is because the deletion process compares the "full" version files against files under 
`<input>` and deletes any files it doesn't find under `<input>`. For most projects, this is useful but for others it 
can be annoying.

If you ever want to modify these project paths or settings, simply use the `edit <project>` command.

### Deploying a project
When you want to deploy your project, use the `deploy <project> <branch>` command.

The arguments are as follows:
1. **Project**: Name of the project you want to deploy
2. **Branch**: Name of the branch/version you want to deploy (more on this later)

Additionally, you may specify the `-F` flag in order to skip separate build directory -creation and 
`-NR` flag to skip the file-deletion process. If these are not specified, the project settings are applied.

Depending on your project composition, the deployment may take less than 1 second or more than one hour.

#### What are branches?
Branches represent different versions of your software, or different use-cases. For example, you may use your 
software in three different environments: One copy for your local development & testing, another for a separate 
test environment and a third version which runs in production. In Optidepy, you would separate these use-cases 
by giving them different branch names (e.g. "dev", "test" and "prod"). When using branches, Optidepy keeps these 
deployments separate, which makes it much easier for you to keep up with changes between multiple versions.

In case you use Optidepy in one environment only (like you often might do with backups, for example), 
just use a single branch name such as "main" or leave the parameter empty.