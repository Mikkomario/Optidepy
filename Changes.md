# Optidepy
List of Changes

## v1.1 (in development)
### Status
- 12.6.2023 - Development started
### Breaking changes
- Build and "full" target directories are now named based on the used branch
### Bugfixes
- Merging now preserves folder structure
- Individual files get deployed
- File removal process now checks file existence from under multiple source bindings
- The deployment algorithm now takes into account renamed directories
### New Features
- Added support for separate deployment branches
- Added new `edit` command
- Added file-removal / -cleaning process
- Added `F` flag to the `deploy` command for disabling build-specific directory-creation
- Added `NR` flag to the `deploy` command for disabling file-removal
- Added support for custom "last deploy time" -specification
### Other Changes
- Tests for file size and/or content changes before considering files changed
- Add project now tests whether a project with the same name exists already
- Added prints to the deployment process

## v1.0.1
### New Features
- Added merge command

## v0.1
Initial Development Version; MVP Features