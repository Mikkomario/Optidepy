# Optidepy
Version: **v1.2**  
Updated: 2024-08-09

## Table of Contents
- [Packages & Classes](#packages-and-classes)
  - [Dependency](#dependency)
    - [Dependency](#dependency)
    - [Dependency Update](#dependency-update)
  - [Deployment](#deployment)
    - [Binding](#binding)
    - [Branch](#branch)
    - [Deployment](#deployment)
    - [Deployment Config](#deployment-config)
  - [Library](#library)
    - [Doc Line Link](#doc-line-link)
    - [Doc Section](#doc-section)
    - [Doc Text](#doc-text)
    - [Module Release](#module-release)
    - [Placed Link](#placed-link)
    - [Sub Section Link](#sub-section-link)
    - [Versioned Module](#versioned-module)
  - [Project](#project)
    - [Project](#project)

## Packages and Classes
Below are listed all classes introduced in Optidepy, grouped by package and in alphabetical order.  
There are a total number of 4 packages and 14 classes

### Dependency
This package contains the following 2 classes: [Dependency](#dependency), [Dependency Update](#dependency-update)

#### Dependency
Represents a dependency of a project / module from a specific library

##### Details
- May combine with [Dependency Update](#dependency-update), creating a **Updated Dependency**
- May combine with [Project](#project), creating a **Project Dependency**
- Preserves **history**
- Uses **index**: `deprecated_after`

##### Properties
Dependency contains the following 6 properties:
- **Dependent Project Id** - `dependentProjectId: Int` - Id of the project which this dependency concerns
  - Refers to [Project](#project)
- **Used Module Id** - `usedModuleId: Int` - Id of the module the referenced project uses
  - Refers to [Versioned Module](#versioned-module)
- **Relative Lib Directory** - `relativeLibDirectory: Path` - Path to the directory where library jars will be placed. 
Relative to the project's root path.
- **Library File Path** - `libraryFilePath: Option[Path]` - Path to the library file matching this dependency. Relative to the project's root path. 
None if not applicable.
- **Created** - `created: Instant` - Time when this dependency was registered
- **Deprecated After** - `deprecatedAfter: Option[Instant]` - Time when this dependency was replaced or removed. None while active.

##### Referenced from
- [Dependency Update](#dependency-update).`dependencyId`

#### Dependency Update
Represents an event where a project's dependency is updated to a new version.

##### Details
- **Chronologically** indexed
- Uses **index**: `created`

##### Properties
Dependency Update contains the following 3 properties:
- **Dependency Id** - `dependencyId: Int` - Id of the dependency this update concerns
  - Refers to [Dependency](#dependency)
- **Release Id** - `releaseId: Int` - Id of the library release that was to the parent project
  - Refers to [Module Release](#module-release)
- **Created** - `created: Instant` - Time when this update was made

### Deployment
This package contains the following 4 classes: [Binding](#binding), [Branch](#branch), [Deployment](#deployment), [Deployment Config](#deployment-config)

#### Binding
Represents a binding between an input directory/file and an output directory/file

##### Details

##### Properties
Binding contains the following 3 properties:
- **Config Id** - `configId: Int` - Id of the configuration this binding belongs to
  - Refers to [Deployment Config](#deployment-config)
- **Source** - `source: Path` - Path to the file or directory that is being deployed. Relative to the input root directory.
- **Target** - `target: Path` - Path to the directory or file where the 'source' is copied. Relative to the root output directory.

#### Branch
Represents a versioned branch of a project

##### Details
- May combine with [Deployment](#deployment), creating a **Deployed Branch**
- May combine with [Project](#project), creating a **Project Branch**
- Preserves **history**
- Uses 2 database **indices**: `deprecated_after`, `is_default`

##### Properties
Branch contains the following 5 properties:
- **Project Id** - `projectId: Int` - Id of the project this branch / version is part of
  - Refers to [Project](#project)
- **Name** - `name: String` - Name of this branch
- **Created** - `created: Instant` - Time when this branch was introduced
- **Deprecated After** - `deprecatedAfter: Option[Instant]` - Time when this branch was removed
- **Is Default** - `isDefault: Boolean` - Whether this is the default branch of the associated project

##### Referenced from
- [Deployment](#deployment).`branchId`

#### Deployment
Represents an event where a project is deployed

##### Details
- **Chronologically** indexed
- Uses 2 database **indices**: `created`, `version`

##### Properties
Deployment contains the following 4 properties:
- **Branch Id** - `branchId: Int` - Id of the branch that was deployed
  - Refers to [Branch](#branch)
- **Index** - `index: Int` - Ordered index of this deployment. Relative to other deployments targeting the same branch.
- **Created** - `created: Instant` - Time when this deployment was made
- **Version** - `version: Option[Version]` - Deployed project version. None if versioning is not being used.

#### Deployment Config
Represents settings used for deploying a project

##### Details
- Combines with possibly multiple [Bindings](#binding), creating a **Detailed Deployment Config**
- May combine with [Project](#project), creating a **Project Deployment Config**
- Preserves **history**
- Uses **index**: `deprecated_after`

##### Properties
Deployment Config contains the following 7 properties:
- **Project Id** - `projectId: Int` - Id of the project which this configuration describes
  - Refers to [Project](#project)
- **Output Directory** - `outputDirectory: Path` - Directory to which all the deployed files / sub-directories will be placed
- **Relative Input Directory** - `relativeInputDirectory: Option[Path]` - Path relative to this project's root directory which contains all deployed files. 
None if the same as the project root path.
- **Created** - `created: Instant` - Time when this configuration was added
- **Deprecated After** - `deprecatedAfter: Option[Instant]` - Time when this configuration was replaced with another
- **Uses Build Directories** - `usesBuildDirectories: Boolean`, `true` by default - Whether this project places deployed files in separate build directories
- **File Deletion Enabled** - `fileDeletionEnabled: Boolean`, `true` by default - Whether output files not present in the input directories should be automatically removed

##### Referenced from
- [Binding](#binding).`configId`

### Library
This package contains the following 7 classes: [Doc Line Link](#doc-line-link), [Doc Section](#doc-section), [Doc Text](#doc-text), [Module Release](#module-release), [Placed Link](#placed-link), [Sub Section Link](#sub-section-link), [Versioned Module](#versioned-module)

#### Doc Line Link
Links a line of text to a document section it appears in

##### Details

##### Properties
Doc Line Link contains the following 3 properties:
- **Section Id** - `sectionId: Int` - Id of the doc section linked with this doc line link
- **Text Id** - `textId: Int` - Id of the doc text linked with this doc line link
- **Order Index** - `orderIndex: Int`, `0` by default - 0-based index determining the location where the linked item is placed

#### Doc Section
Represents section within a documentation file

##### Details
- May combine with [Doc Text](#doc-text), creating a **Doc Section With Header**
- May combine with [Sub Section Link](#sub-section-link), creating a **Sub Section**

##### Properties
Doc Section contains the following 2 properties:
- **Header Id** - `headerId: Int` - Id of the header (text) of this section.
  - Refers to [Doc Text](#doc-text)
- **Created** - `created: Instant` - Time when this section (version) was added

##### Referenced from
- [Module Release](#module-release).`docId`

#### Doc Text
Contains a single piece of line of text which is present in some documentation

##### Details
- May combine with [Doc Line Link](#doc-line-link), creating a **Placed Doc Line**

##### Properties
Doc Text contains the following 3 properties:
- **Text** - `text: String` - Wrapped text contents
- **Indentation** - `indentation: Int`, `0` by default - Level of indentation applicable to this line / text
- **Created** - `created: Instant` - Time when this text was first introduced

##### Referenced from
- [Doc Section](#doc-section).`headerId`

#### Module Release
Represents a published version / build of a versioned module

##### Details

##### Properties
Module Release contains the following 4 properties:
- **Module Id** - `moduleId: Int` - Id of the released module
  - Refers to [Versioned Module](#versioned-module)
- **Version** - `version: Version` - Released version
- **Jar Name** - `jarName: String` - Name of the generated jar file.
- **Doc Id** - `docId: Option[Int]` - Id of the documentation of this release. None if there is no documentation specific to this version.
  - Refers to [Doc Section](#doc-section)

##### Referenced from
- [Dependency Update](#dependency-update).`releaseId`

#### Placed Link
Common trait for links which place the referenced item to a specific location within the parent item

##### Details

##### Properties
Placed Link contains the following 3 properties:
- **Parent Id** - `parentId: Int` - Id of the element within which the linked item is placed
- **Child Id** - `childId: Int` - Id of the linked / placed element
- **Order Index** - `orderIndex: Int`, `0` by default - 0-based index determining the location where the linked item is placed

#### Sub Section Link
Represents a link between two doc sections, assigning one as the subsection of another

##### Details

##### Properties
Sub Section Link contains the following 3 properties:
- **Parent Id** - `parentId: Int` - Id of the parent section, which contains the child section
- **Child Id** - `childId: Int` - Id of the section contained within the parent section
- **Order Index** - `orderIndex: Int`, `0` by default - 0-based index determining the location where the linked item is placed

#### Versioned Module
Represents a library module which may be exported or added as a dependency to another project

##### Details
- May combine with [Module Release](#module-release), creating a **Released Module**
- May combine with [Project](#project), creating a **Versioned Project Module**
- Fully **versioned**
- Uses 3 database **indices**: `name`, `created`, `deprecated_after`

##### Properties
Versioned Module contains the following 6 properties:
- **Project Id** - `projectId: Int` - Id of the project this module is part of
  - Refers to [Project](#project)
- **Name** - `name: String` - Name of this module
- **Relative Change List Path** - `relativeChangeListPath: Path` - A path relative to the project root directory, which points to this module's Changes.md file
- **Relative Artifact Directory** - `relativeArtifactDirectory: Path` - A path relative to the project root directory, which points to the directory where artifact jar files will be exported
- **Created** - `created: Instant` - Time when this module was introduced
- **Deprecated After** - `deprecatedAfter: Option[Instant]` - Time when this module was archived or removed

##### Referenced from
- [Dependency](#dependency).`usedModuleId`
- [Module Release](#module-release).`moduleId`

### Project
This package contains the following 1 classes: [Project](#project)

#### Project
Represents a project that may be deployed, released or updated

##### Details
- Preserves **history**
- Uses 2 database **indices**: `name`, `deprecated_after`

##### Properties
Project contains the following 4 properties:
- **Name** - `name: String` - Name of this project
- **Root Path** - `rootPath: Path` - Path to the directory that contains this project
- **Created** - `created: Instant` - Time when this project was introduced
- **Deprecated After** - `deprecatedAfter: Option[Instant]` - Time whe this project was removed / archived

##### Referenced from
- [Branch](#branch).`projectId`
- [Dependency](#dependency).`dependentProjectId`
- [Deployment Config](#deployment-config).`projectId`
- [Versioned Module](#versioned-module).`projectId`
