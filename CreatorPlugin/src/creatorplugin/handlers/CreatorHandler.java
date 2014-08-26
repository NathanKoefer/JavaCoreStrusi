package creatorplugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CreatorHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public CreatorHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		/*		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"CreatorPlugin",
				"Hello, Eclipse world");*/
		//createProject();	
		try {
			createTestProject();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

	private void createTestProject() throws CoreException{

		IProgressMonitor progressMonitor = new NullProgressMonitor();


		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject("TESTJDT");
		project.create(null);
		project.open(null);

		//set the Java nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });

		//create the project
		project.setDescription(description, null);
		IJavaProject javaProject = JavaCore.create(project);

		//set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(project.getFullPath().append("src")),
				JavaRuntime.getDefaultJREContainerEntry() };

		javaProject.setRawClasspath(buildPath, project.getFullPath().append(
				"bin"), null);

		//create folder by using resources package
		IFolder folder = project.getFolder("src");
		folder.create(true, true, null);

		//Add folder to Java element
		IPackageFragmentRoot srcFolder = javaProject
				.getPackageFragmentRoot(folder);

		//create package fragment
		IPackageFragment packageFragment = srcFolder.createPackageFragment(
				"org.supsi", true, null);
		
		IPackageFragment packageFragment2 = srcFolder.createPackageFragment(
				"org.supsi2", true, null);
		
		

		//init code string and create compilation unit
		String str = "package org.supsi2;" + "\n"
				+ "public class Test  {" + "\n" + " private static String name = \"The name\";"
				+ "\n" + "}";

		ICompilationUnit cu = packageFragment2.createCompilationUnit("Test.java", str,
				false, null);

		//create a field
		IType type = cu.getType("Test");

		String methodContent = "public static void metodo(){" + "\n"
				+ "System.out.println(name);" + "\n"
				+ "}";		

		//type.createField("private String age;", null, true, null);
		type.createMethod(methodContent, null, false, progressMonitor);


		String mainTestContent = "package org.supsi;" + "\n"
				+"class MainTest {" + "\n"
				+ "public static void main(String[] args) {" + "\n"
				+ "Test.metodo();" + "\n"
				+ "}" + "\n"
				+ "}";


		ICompilationUnit cu2 = packageFragment.createCompilationUnit("MainTest.java", mainTestContent, false, progressMonitor);


	}

	private void createProject(){

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		String name = "MyProject";
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root= workspace.getRoot();
		IProject project= root.getProject(name);
		try {
			project.create(null);
			project.open(null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IProjectDescription desc = null;
		try {
			desc = project.getDescription();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		desc.setNatureIds(new String[] {
				JavaCore.NATURE_ID});
		try {
			project.setDescription(desc, progressMonitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		IJavaProject javaProj = JavaCore.create(project);
		IFolder binDir = project.getFolder("bin");
		IPath binPath = binDir.getFullPath();
		try {
			javaProj.setOutputLocation(binPath, progressMonitor);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		String path = "C:\\Program Files\\Java\\jre1.8.0_20\\lib\\rt.jar";
		IPath iPath = new Path(path);
		IClasspathEntry cpe= JavaCore.newLibraryEntry(iPath, null, null);
		try {
			javaProj.setRawClasspath(new IClasspathEntry[] {cpe}, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			createPackage(project);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private void createPackage(IProject project) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		IFolder folder = project.getFolder("src");
		// folder.create(true, true, null);
		IPackageFragmentRoot srcFolder = javaProject
				.getPackageFragmentRoot(folder);
		IPackageFragment fragment = srcFolder.createPackageFragment(project.getName(), true, null);
	}

}
