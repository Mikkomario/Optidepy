-- 
-- Database structure for optidepy models
-- Version: v1.2
-- Last generated: 2024-08-09
--

CREATE DATABASE IF NOT EXISTS `optidepy` 
	DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
USE `optidepy`;

--	Project	----------

-- Represents a project that may be deployed, released or updated
-- name:             Name of this project
-- root_path:        Path to the directory that contains this project
-- created:          Time when this project was introduced
-- deprecated_after: Time whe this project was removed / archived
CREATE TABLE `project`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`name` VARCHAR(16) NOT NULL, 
	`root_path` VARCHAR(255), 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`deprecated_after` DATETIME, 
	INDEX p_name_idx (`name`), 
	INDEX p_deprecated_after_idx (`deprecated_after`)
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;


--	Library	----------

-- Links a line of text to a document section it appears in
-- section_id:  Id of the doc section linked with this doc line link
-- text_id:     Id of the doc text linked with this doc line link
-- order_index: 0-based index determining the location where the linked item is placed
CREATE TABLE `doc_line_link`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`section_id` INT NOT NULL, 
	`text_id` INT NOT NULL, 
	`order_index` TINYINT NOT NULL DEFAULT 0
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Contains a single piece of line of text which is present in some documentation
-- text:        Wrapped text contents
-- indentation: Level of indentation applicable to this line / text
-- created:     Time when this text was first introduced
CREATE TABLE `doc_text`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`text` VARCHAR(64), 
	`indentation` TINYINT NOT NULL DEFAULT 0, 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents a link between two doc sections, assigning one as the subsection of another
-- parent_id:   Id of the parent section, which contains the child section
-- child_id:    Id of the section contained within the parent section
-- order_index: 0-based index determining the location where the linked item is placed
CREATE TABLE `sub_section_link`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`parent_id` INT NOT NULL, 
	`child_id` INT NOT NULL, 
	`order_index` TINYINT NOT NULL DEFAULT 0
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents a library module which may be exported or added as a dependency to another project
-- project_id:                  Id of the project this module is part of
-- name:                        Name of this module
-- relative_change_list_path:   A path relative to the project root directory, which points to this module's Changes.md file
-- relative_artifact_directory: A path relative to the project root directory, which points to the directory where artifact jar files will be exported
-- created:                     Time when this module was introduced
-- deprecated_after:            Time when this module was archived or removed
CREATE TABLE `versioned_module`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`project_id` INT NOT NULL, 
	`name` VARCHAR(12) NOT NULL, 
	`relative_change_list_path` VARCHAR(64), 
	`relative_artifact_directory` VARCHAR(128), 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`deprecated_after` DATETIME, 
	INDEX vm_name_idx (`name`), 
	INDEX vm_created_idx (`created`), 
	INDEX vm_deprecated_after_idx (`deprecated_after`), 
	CONSTRAINT vm_p_project_ref_fk FOREIGN KEY vm_p_project_ref_idx (project_id) REFERENCES `project`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents section within a documentation file
-- header_id: Id of the header (text) of this section.
-- created:   Time when this section (version) was added
CREATE TABLE `doc_section`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`header_id` INT NOT NULL, 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	CONSTRAINT ds_dt_header_ref_fk FOREIGN KEY ds_dt_header_ref_idx (header_id) REFERENCES `doc_text`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents a published version / build of a versioned module
-- module_id: Id of the released module
-- version:   Released version
-- jar_name:  Name of the generated jar file.
-- doc_id:    Id of the documentation of this release. None if there is no documentation specific to this version.
CREATE TABLE `module_release`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`module_id` INT NOT NULL, 
	`version` VARCHAR(255), 
	`jar_name` VARCHAR(16), 
	`doc_id` INT, 
	CONSTRAINT mr_vm_module_ref_fk FOREIGN KEY mr_vm_module_ref_idx (module_id) REFERENCES `versioned_module`(`id`) ON DELETE CASCADE, 
	CONSTRAINT mr_ds_doc_ref_fk FOREIGN KEY mr_ds_doc_ref_idx (doc_id) REFERENCES `doc_section`(`id`) ON DELETE SET NULL
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;


--	Deployment	----------

-- Represents a versioned branch of a project
-- project_id:       Id of the project this branch / version is part of
-- name:             Name of this branch
-- created:          Time when this branch was introduced
-- deprecated_after: Time when this branch was removed
-- is_default:       Whether this is the default branch of the associated project
CREATE TABLE `branch`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`project_id` INT NOT NULL, 
	`name` VARCHAR(8) NOT NULL, 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`deprecated_after` DATETIME, 
	`is_default` BOOLEAN NOT NULL DEFAULT FALSE, 
	INDEX br_deprecated_after_idx (`deprecated_after`), 
	INDEX br_is_default_idx (`is_default`), 
	CONSTRAINT br_p_project_ref_fk FOREIGN KEY br_p_project_ref_idx (project_id) REFERENCES `project`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents settings used for deploying a project
-- project_id:               Id of the project which this configuration describes
-- output_directory:         Directory to which all the deployed files / sub-directories will be placed
-- relative_input_directory: Path relative to this project's root directory which contains all deployed files. 
-- 		None if the same as the project root path.
-- created:                  Time when this configuration was added
-- deprecated_after:         Time when this configuration was replaced with another
-- uses_build_directories:   Whether this project places deployed files in separate build directories
-- file_deletion_enabled:    Whether output files not present in the input directories should be automatically removed
CREATE TABLE `deployment_config`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`project_id` INT NOT NULL, 
	`output_directory` VARCHAR(255), 
	`relative_input_directory` VARCHAR(128), 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`deprecated_after` DATETIME, 
	`uses_build_directories` BOOLEAN NOT NULL DEFAULT TRUE, 
	`file_deletion_enabled` BOOLEAN NOT NULL DEFAULT TRUE, 
	INDEX dc_deprecated_after_idx (`deprecated_after`), 
	CONSTRAINT dc_p_project_ref_fk FOREIGN KEY dc_p_project_ref_idx (project_id) REFERENCES `project`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents a binding between an input directory/file and an output directory/file
-- config_id: Id of the configuration this binding belongs to
-- source:    Path to the file or directory that is being deployed. Relative to the input root directory.
-- target:    Path to the directory or file where the 'source' is copied. Relative to the root output directory.
CREATE TABLE `binding`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`config_id` INT NOT NULL, 
	`source` VARCHAR(128), 
	`target` VARCHAR(128), 
	CONSTRAINT bi_dc_config_ref_fk FOREIGN KEY bi_dc_config_ref_idx (config_id) REFERENCES `deployment_config`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents an event where a project is deployed
-- branch_id: Id of the branch that was deployed
-- index:     Ordered index of this deployment. Relative to other deployments targeting the same branch.
-- created:   Time when this deployment was made
-- version:   Deployed project version. None if versioning is not being used.
CREATE TABLE `deployment`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`branch_id` INT NOT NULL, 
	`index` INT NOT NULL, 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`version` VARCHAR(255), 
	INDEX depl_created_idx (`created`), 
	INDEX depl_version_idx (`version`), 
	CONSTRAINT depl_br_branch_ref_fk FOREIGN KEY depl_br_branch_ref_idx (branch_id) REFERENCES `branch`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;


--	Dependency	----------

-- Represents a dependency of a project / module from a specific library
-- dependent_project_id:   Id of the project which this dependency concerns
-- used_module_id:         Id of the module the referenced project uses
-- relative_lib_directory: Path to the directory where library jars will be placed. 
-- 		Relative to the project's root path.
-- library_file_path:      Path to the library file matching this dependency. Relative to the project's root path. 
-- 		None if not applicable.
-- created:                Time when this dependency was registered
-- deprecated_after:       Time when this dependency was replaced or removed. None while active.
CREATE TABLE `dependency`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`dependent_project_id` INT NOT NULL, 
	`used_module_id` INT NOT NULL, 
	`relative_lib_directory` VARCHAR(24), 
	`library_file_path` VARCHAR(255), 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`deprecated_after` DATETIME, 
	INDEX depe_deprecated_after_idx (`deprecated_after`), 
	CONSTRAINT depe_p_dependent_project_ref_fk FOREIGN KEY depe_p_dependent_project_ref_idx (dependent_project_id) REFERENCES `project`(`id`) ON DELETE CASCADE, 
	CONSTRAINT depe_vm_used_module_ref_fk FOREIGN KEY depe_vm_used_module_ref_idx (used_module_id) REFERENCES `versioned_module`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- Represents an event where a project's dependency is updated to a new version.
-- dependency_id: Id of the dependency this update concerns
-- release_id:    Id of the library release that was to the parent project
-- created:       Time when this update was made
CREATE TABLE `dependency_update`(
	`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	`dependency_id` INT NOT NULL, 
	`release_id` INT NOT NULL, 
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	INDEX du_created_idx (`created`), 
	CONSTRAINT du_depe_dependency_ref_fk FOREIGN KEY du_depe_dependency_ref_idx (dependency_id) REFERENCES `dependency`(`id`) ON DELETE CASCADE, 
	CONSTRAINT du_mr_release_ref_fk FOREIGN KEY du_mr_release_ref_idx (release_id) REFERENCES `module_release`(`id`) ON DELETE CASCADE
)Engine=InnoDB DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

