package vf.optidepy.database

import utopia.vault.model.immutable.Table

/**
  * Used for accessing the database tables introduced in this project
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object OptidepyTables
{
	// COMPUTED	--------------------
	
	/**
	  * Table that contains bindings (Represents a binding between an input directory/file and an
	  *  output directory/file)
	  */
	def binding = apply("binding")
	
	/**
	  * Table that contains branches (Represents a versioned branch of a project)
	  */
	def branch = apply("branch")
	
	/**
	  * Table that contains dependencies (Represents a dependency of a project / 
		module from a specific library)
	  */
	def dependency = apply("dependency")
	
	/**
	  * Table that contains dependency updates (Represents an event where a project's dependency is updated to
	  *  a new version.)
	  */
	def dependencyUpdate = apply("dependency_update")
	
	/**
	  * Table that contains deployments (Represents an event where a project is deployed)
	  */
	def deployment = apply("deployment")
	
	/**
	  * Table that contains deployment configs (Represents settings used for deploying a project)
	  */
	def deploymentConfig = apply("deployment_config")
	
	/**
	  * Table that contains doc line links (Links a line of text to a document section it appears in)
	  */
	def docLineLink = apply("doc_line_link")
	
	/**
	  * Table that contains doc sections (Represents section within a documentation file)
	  */
	def docSection = apply("doc_section")
	
	/**
	  * 
		Table that contains doc texts (Contains a single piece of line of text which is present in some documentation)
	  */
	def docText = apply("doc_text")
	
	/**
	  * Table that contains module releases (Represents a published version / build of a versioned module)
	  */
	def moduleRelease = apply("module_release")
	
	/**
	  * Table that contains projects (Represents a project that may be deployed, released or updated)
	  */
	def project = apply("project")
	
	/**
	  * Table that contains sub section links (Represents a link between two doc sections, 
	  * assigning one as the subsection of another)
	  */
	def subSectionLink = apply("sub_section_link")
	
	/**
	  * Table that contains versioned modules (Represents a library module which may be exported or added as
	  *  a dependency to another project)
	  */
	def versionedModule = apply("versioned_module")
	
	
	// OTHER	--------------------
	
	private def apply(tableName: String): Table = {
		// TODO: Refer to a tables instance of your choice
		// If you're using the Citadel module, import utopia.citadel.database.Tables
		// Tables(tableName)
		???
	}
}

