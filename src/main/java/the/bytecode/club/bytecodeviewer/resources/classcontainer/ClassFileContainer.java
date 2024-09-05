package the.bytecode.club.bytecodeviewer.resources.classcontainer;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import the.bytecode.club.bytecodeviewer.resources.classcontainer.locations.ClassFieldLocation;
import the.bytecode.club.bytecodeviewer.resources.classcontainer.locations.ClassLocalVariableLocation;
import the.bytecode.club.bytecodeviewer.resources.classcontainer.locations.ClassMethodLocation;
import the.bytecode.club.bytecodeviewer.resources.classcontainer.locations.ClassParameterLocation;
import the.bytecode.club.bytecodeviewer.resources.classcontainer.parser.MyVoidVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by Bl3nd.
 * Date: 8/26/2024
 */
public class ClassFileContainer
{
	public transient NavigableMap<String, ArrayList<ClassFieldLocation>> fieldMembers = new TreeMap<>();
	public transient NavigableMap<String, ArrayList<ClassParameterLocation>> methodParameterMembers = new TreeMap<>();
	public transient NavigableMap<String, ArrayList<ClassLocalVariableLocation>> methodLocalMembers = new TreeMap<>();
	public transient NavigableMap<String, ArrayList<ClassMethodLocation>> methodMembers = new TreeMap<>();

	public boolean hasBeenParsed = false;
	public final String className;
	private final String content;

	public ClassFileContainer(String className, String content)
	{
		this.className = className;
		this.content = content;
	}

	public void parse()
	{
		try
		{
			StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
			CompilationUnit compilationUnit = StaticJavaParser.parse(this.content);
			compilationUnit.accept(new MyVoidVisitor(this, compilationUnit), null);
		} catch (ParseProblemException e)
		{
			System.err.println("Parsing error!");
		}
	}

	public void putField(String key, ClassFieldLocation value)
	{
		this.fieldMembers.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
	}

	public List<ClassFieldLocation> getFieldLocationsFor(String fieldName)
	{
		return fieldMembers.getOrDefault(fieldName, new ArrayList<>());
	}

	public void putParameter(String key, ClassParameterLocation value)
	{
		this.methodParameterMembers.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
	}

	public List<ClassParameterLocation> getParameterLocationsFor(String key)
	{
		return methodParameterMembers.getOrDefault(key, new ArrayList<>());
	}

	public void putLocalVariable(String key, ClassLocalVariableLocation value)
	{
		this.methodLocalMembers.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
	}

	public List<ClassLocalVariableLocation> getLocalLocationsFor(String key)
	{
		return methodLocalMembers.getOrDefault(key, new ArrayList<>());
	}

	public void putMethod(String key, ClassMethodLocation value)
	{
		this.methodMembers.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
	}

	public List<ClassMethodLocation> getMethodLocationsFor(String key)
	{
		return methodMembers.getOrDefault(key, new ArrayList<>());
	}
}
