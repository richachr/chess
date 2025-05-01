# Chess Project Notes

- Use git mv instead of mv to rename/move files, use git rm to remove files. git rm --cached removes a file from
  repository only.
- Math methods allow for safer calculations, bigger numbers.
- final keyword declares a constant variable.
- Variables must be initialized before use.
- A package is a set of related classes.
- Static methods are not attached to any object.
- Casting is done by adding (type) to the beginning ex. (float) 3.14
- Use .equals() to compare strings, not ==, except for checking nullity.
- Integer.toString() converts an int to a string, Integer.parseInt() converts a string to an int.
- Strings are immutable.
- Adding \s to the end of a string preserves trailing spaces.
- var declaration allows for generic typing.
- To read input, create a new Scanner(System.in).
- scannerName.nextLine() reads next line, next() reads next word, nextInt(), etc.
- System.console() is slightly safer because of array storage overwrites.
- System.out.println() prints with newline, print() prints with no newline.
- Printf format string: % precedes a one-letter type identifier (f,s,d, etc.), .#f with a number allows shortened
  precision.
- Using L/f/d ending tags for long/float/double literals saves runtime and prevents errors.
- String.format() can use formatted strings to combine/format strings.
- Using the new String() constructor negates the benefits of interning/string optimization. 
- Special characters: \n, \t, \", \', \\, \b, \uXXXX
- String concatenation is inefficient because it creates new strings, so use a StringBuilder.
- If, else if, else are normal, can be shortened with ? : .
- Constructors have no return type, have the same name as class.
- StringBuilder class allows you to modify the string over time.
- Arrays are allocated similar to cpp, but the type is Type[] instead of Type*.
- Switches map inputs to outputs : switch (input) { case value[, value2, ...] -> output var or statement; ... default ->
  {if it's a block, it must have a yield.} } Nulls can cause errors, so have a case null. Switches can fall-through (
  execute following cases until stopped) by using : instead of -> and using yield or break to stop falling.
- Labeled break statements can jump out of multiple loops. Syntax: breakLabel: {code} break breakLabel;
- Variables in overlapping scopes must have unique names.
- All arrays have default values, except for objects, where the array is full of null references.
- Arrays have fixed sizes, ArrayLists don't.
- Classes can be typed like C++.
- Elements can be added with .add([index], val) and removed with .remove([index], val), .get(index) gets a value, .set(
  index, value) sets a value.
- ArrayLists cannot have primitive types, so they use wrappers, such as Integer. .equals() must be used to check
  equality with these, as == compares references.
- Arrays are passed by reference, so separating them requires using Arrays.copyOf(array, length).
- To copy an ArrayList, you can use new ArrayList<type>(arrayList);, or List.of(array).
- Arrays have methods like fill and sort, to use these for ArrayLists use Collections class.
- Args start at 0, without the program name included.
- Arrays.deepToString(array) prints nested lists.
- Each function must specify publicity and return type, and if it is static.
- Varargs are declared as the last parameter using method(type... name)
- InputStream and OutputStream objects are created using Files.newInputStream(path);
- stream.read() reads a single byte, .readAllBytes() reads all bytes (can also be done with Files.readAllBytes(path)),
  or read N number of bytes with .readNBytes(), skipping N bytes with skipNBytes()
- .write() can write bytes/byte arrays, but it must be closed, which can be done using a try-with-resources statement.
  An input stream can be transferred to an output stream by using in.transferTo(out). Files can be copied using
  Files.copy(path, out)
- Encoding can be specified with a Charset object, found in StandardCharsets or with Charset.forName("name")
- Text can be read using a reader, such as with a new InputStreamReader(stream, charset). Short text can be read with
  Files.readString(path, charset), or a sequence of lines as Files.readAllLines(path,charset). Large files can be
  processed as a Stream<String> with Files.lines(path, charset) and a try-with-resources. To read numbers or words, use
  a scanner and its delimiter.
- To read from a file, use Files.newBufferedReader(). To read from a non-file, wrap an inputStream in a new BufferedReader(stream).
- To write text, use an OutputStreamWriter(stream, charset) and out.write(str). For a file, use Files.newBufferedWriter(path, charset).
- For convenience, use a new PrintWriter(streamWriter) to get print methods.
- To write a preprepared string, use Files.write/writeString(path,string,charset,[StandardOpenOption.APPEND])
- You can lock files using FileChannel and FileLock classes.
- File paths can be created using the Path.of() method.
- Paths can be combined using path.resolve(secondPath). path.relativize returns the relative path from one to another.
- Working directory can be fetched using System.getProperty("user.dir").
- To create a new directory, use Files.createDirectory(path). For multiple, use Files.createDirectories(path). To create a file. use Files.createFile(path). Files.exists checks if it exists. Temp files/directories can be created using createTempFile(path, prefix, suffix), etc.
- To copy, use Files.copy(from, to). To move, use Files.move(from, to). Replace existing and copy attributes can be included as params with StandardCopyOption.REPLACE_EXISTING, etc.
- FIles.delete(path) deletes a file.
- FIles.list(path) lists all files as a Stream of paths, should be opened as try-with-resources. Depth-first.
- Imports don't increase size of compiled files. They appear after package but before class declarations. * imports all packages, but not subpackages. No import is needed for classes in java.lang or in the same package.
- Classpath manages directories for packages
