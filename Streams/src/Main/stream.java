package Main;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class stream {

	public static void main(String[] args) {

		Stream<String> lista = Stream.generate(() -> "ABC");
		lista

		stream.forEach(System.out::println);
	}

}
