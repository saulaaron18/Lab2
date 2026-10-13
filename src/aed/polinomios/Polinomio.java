package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;


/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

	// Una lista de monomios -- NO CAMBIA a private; el tester necesita acceso
	PositionList<Monomio> terms;

	/**
	 * Crea un polinomio representando el valor 0.
	 */
	public Polinomio() {
		terms = new NodePositionList<>();
	}

	/**
	 * Crea un polinomio definado por una lista con monomios.
	 * @param terms una lista de monomios
	 */
	public Polinomio(PositionList<Monomio> terms) {
		this.terms = terms;
	}

	/**
	 * Crea un polinomio definado por un String.
	 * La representación del polinomio es una secuencia de monomios separados
	 * por '+' (y posiblemente con caracteres blancos).
	 * Un monomio esta compuesto por tres partes,
	 * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
	 * compuesto por un un caracter '^' seguido por un entero.
	 * Se puede omitir multiples partes de un monomio, 
	 * ejemplos:
	 * <pre>
	 * {@code
	 * new Polinomio("2x^3 + 9");
	 * new Polinomio("2x^3 + -9");
	 * new Polinomio("x");   // == 1x^1
	 * new Polinomio("5");   // == 5x^0
	 * new Polinomio("8x");  // == 8x^1
	 * new Polinomio("0");   // == 0x^0
	 * }
	 * </pre>
	 * @throws IllegalArgumentException si el argumento es malformado
	 * @param polinomio - una secuencia de monomios separados por '+'
	 */
	public Polinomio(String polinomio) {
		if(polinomio == null) {
			throw new IllegalArgumentException();
		}
		PositionList<Monomio> polinomioResultante = new NodePositionList<>();
		String[] arrayPol = polinomio.split("\\+");

		for(int i=0;i<arrayPol.length;i++) {
			arrayPol[i] = arrayPol[i].trim();

			int coef = coeficiente(arrayPol[i]);
			if(coef==0) continue;
			int exp = exponente(arrayPol[i]);

			polinomioResultante.addLast(new Monomio(coef, exp));
		}

		terms = polinomioResultante;
	}



	/**
	 * Suma dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return la suma de los polinomios.
	 */
	public static Polinomio suma(Polinomio p1, Polinomio p2) {
		return sumaResta(p1, p2, true);
	}

	/**
	 * Substraccion de dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return la resta de los polinomios.
	 */
	public static Polinomio resta(Polinomio p1, Polinomio p2) {
		return sumaResta(p1, p2, false);
	}

	/**
	 * Calcula el producto de dos polinomios.
	 * @param p1 primer polinomio.
	 * @param p2 segundo polinomio.
	 * @return el producto de los polinomios.
	 */
	public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
		Polinomio acum = new Polinomio();
		Position<Monomio> cursor = p1.terms.first();
		
		while(cursor != null){
			acum = suma(acum,multiplica(cursor.element(),p2));
			cursor = p1.terms.next(cursor);
		}
		
		return acum;
	}

	/**
	 * Calcula el producto de un monomio y un polinomio.
	 * @param m el monomio
	 * @param p el polinomio
	 * @return el producto del monomio y el polinomio
	 */
	public static Polinomio multiplica(Monomio m, Polinomio p) {
		Position<Monomio> cursor = p.terms.first();
		Polinomio res = new Polinomio();
		
		while(cursor != null){
			res.terms.addLast(new Monomio(m.coeficiente()*cursor.element().coeficiente(),
					m.exponente()+cursor.element().exponente()));
			cursor = p.terms.next(cursor);
		}
		
		return res;
	}

	/**
	 * Devuelve el exponente (grado) del monomio con el mayor grado
	 * dentro del polinomio
	 * Si el polinomio es vacio (la representacion del polinomio "0") entonces
	 * el valor devuelto debe ser -1.
	 * @return el grado del polinomio
	 */
	public int grado() {
		if(terms.isEmpty()) {
			return -1;
		}

		return terms.first().element().exponente();
	}

	/**
	 * Cambia las occurrencias del variable "x" dentro el polinomio p por el polinomio arg.
	 * @return El polinomio p donde el variable "x" esta sustuido por el polinomio arg.
	 * Ejemplo:
	 * <pre>
	 * {@code
	 * Polinomio p1 = new Polinomio("2x^3 + 9");
	 * Polinomio p2 = new Polinomio("x + 1");
	 * Polinomio.componer(p1,p2) =>
	 *   2 * (x+1)^3 + 9 = 2x^3 + 6x^2 + 6x^1 + 11
	 * }
	 * </pre>
	 *
	 */
	public static Polinomio componer(Polinomio p, Polinomio arg) {
		return null;
	}

	/**
	 * Calcula el derivado de un polinomio.
	 * @return el polinomio representando el derivado.
	 */
	public Polinomio derivada() {
		PositionList<Monomio> termsResult = new NodePositionList<>();

		for(Monomio mon : terms) {
			int exp = mon.exponente()-1;

			if(exp==-1) {
				continue;
			}

			int coef = mon.coeficiente()*mon.exponente();

			termsResult.addLast(new Monomio(coef, exp));
		}

		return new Polinomio(termsResult);
	}

	@Override
	public int hashCode() {
		return terms.hashCode();
	}

	@Override
	public String toString() {
		if (terms.isEmpty()) return "0";
		else {
			StringBuffer buf = new StringBuffer();
			Position<Monomio> cursor = terms.first();
			while (cursor != null) {
				Monomio p = cursor.element();
				int coef = p.coeficiente();
				int exp = p.exponente();
				buf.append(new Integer(coef).toString());
				if (exp > 0) {
					buf.append("x");
					buf.append("^");
					buf.append(new Integer(exp)).toString();
				}
				cursor = terms.next(cursor);
				if (cursor != null) buf.append(" + ");
			}
			return buf.toString();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Polinomio)) {
			return false;
		}

		Polinomio pol = (Polinomio) obj;

		return pol.terms.equals(this.terms);
	}


	// Métodos privados de los ejercicios obligatorios
	private static Polinomio sumaResta(Polinomio p1, Polinomio p2, boolean esSuma) {
		PositionList<Monomio> resultado = new NodePositionList<>();
		BiFunction<Integer, Integer, Integer> op = (a, b) -> (esSuma) ? a+b : a-b;

		Position<Monomio> pos1 = primero(p1);
		Position<Monomio> pos2 = primero(p2);

		while (pos1 != null || pos2 != null) {
			int cmp = comparar(pos1, pos2);

			if (cmp > 0) { //exp1>exp2 || pos2==null
				Monomio mon1 = pos1.element();

				agregar(resultado, mon1.coeficiente(), mon1.exponente());
				pos1 = p1.terms.next(pos1);
			} else if (cmp < 0) { //exp1<exp2 || pos1==null
				Monomio mon2 = pos2.element();

				agregar(resultado, op.apply(0, mon2.coeficiente()), mon2.exponente());
				pos2 = p2.terms.next(pos2);
			} else { //exp1==exp2
				Monomio mon1 = pos1.element();
				Monomio mon2 = pos2.element();

				agregar(resultado, op.apply(mon1.coeficiente(), mon2.coeficiente()), mon1.exponente());
				pos1 = p1.terms.next(pos1);
				pos2 = p2.terms.next(pos2);
			}
		}
		return new Polinomio(resultado);
	}

	private static void agregar(PositionList<Monomio> lista, int coef, int exp) {
		if (coef != 0) {
			lista.addLast(new Monomio(coef, exp));
		}
	}

	private static int comparar(Position<Monomio> pos1, Position<Monomio> pos2) {
		if (pos2 == null) {
			return 1;
		}
		if (pos1 == null) {
			return -1;
		}

		return Integer.compare(pos1.element().exponente(), pos2.element().exponente());
	}

	private static Position<Monomio> primero(Polinomio p) {
		return (p.terms.isEmpty()) ? null : p.terms.first();
	}

	// Metodos Privados para el Constructor (ejercicio opcional)
	private int coeficiente(String mon) {
		boolean esNegativo = mon.startsWith("-");
		int cotaInferior = (esNegativo) ? 1 : 0;
		int cotaSuperior = mon.indexOf("x");

		if(cotaInferior==cotaSuperior) {
			return (esNegativo) ? -1 : 1;
		}

		return Integer.parseInt(
				mon.substring(0, 
						(cotaSuperior<cotaInferior) ? mon.length() : cotaSuperior));
	}

	private int exponente(String mon) {
		int indexSymbExp = mon.indexOf("^");

		if(indexSymbExp != -1) {
			return Integer.parseInt(mon.substring(indexSymbExp+1, mon.length()));
		}

		if(mon.contains("x")) {
			return 1;
		}

		return 0;
	}
}
