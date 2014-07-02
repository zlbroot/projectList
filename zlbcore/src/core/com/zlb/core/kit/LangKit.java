package com.zlb.core.kit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 这些帮助函数让 Java 的某些常用功能变得更简单
 */
public abstract class LangKit {

	/**
	 * 生成一个未实现的运行时异常
	 * 
	 * @return 一个未实现的运行时异常
	 */
	public static RuntimeException noImplement() {
		return new RuntimeException("Not implement yet!");
	}

	/**
	 * 生成一个不可能的运行时异常
	 * 
	 * @return 一个不可能的运行时异常
	 */
	public static RuntimeException impossible() {
		return new RuntimeException("r u kidding me?! It is impossible!");
	}

	/**
	 * 根据格式化字符串，生成运行时异常
	 * 
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException makeThrow(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	/**
	 * 将抛出对象包裹成运行时异常，并增加自己的描述
	 * 
	 * @param e
	 *            抛出对象
	 * @param fmt
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e, String fmt,
			Object... args) {
		return new RuntimeException(String.format(fmt, args), e);
	}

	/**
	 * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回。
	 * <p>
	 * 如果是 InvocationTargetException，那么将其剥离，只包裹其 TargetException
	 * 
	 * @param e
	 *            抛出对象
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		if (e instanceof InvocationTargetException)
			return wrapThrow(((InvocationTargetException) e)
					.getTargetException());
		return new RuntimeException(e);
	}

	public static Throwable unwrapThrow(Throwable e) {
		if (e == null)
			return null;
		if (e instanceof InvocationTargetException) {
			InvocationTargetException itE = (InvocationTargetException) e;
			if (itE.getTargetException() != null)
				return unwrapThrow(itE.getTargetException());
		}
		if (e instanceof RuntimeException && e.getCause() != null)
			return unwrapThrow(e.getCause());
		return e;
	}

	/**
	 * 从一个文本输入流读取所有内容，并将该流关闭
	 * 
	 * @param reader
	 *            文本输入流
	 * @return 输入流所有内容
	 */
	public static String readAll(Reader reader) {
		if (!(reader instanceof BufferedReader))
			reader = new BufferedReader(reader);
		try {
			StringBuilder sb = new StringBuilder();

			char[] data = new char[64];
			int len;
			while (true) {
				if ((len = reader.read(data)) == -1)
					break;
				sb.append(data, 0, len);
			}
			return sb.toString();
		} catch (IOException e) {
			throw wrapThrow(e);
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					wrapThrow(e);
				}
			}
		}
	}

	/**
	 * 将一段字符串写入一个文本输出流，并将该流关闭
	 * 
	 * @param writer
	 *            文本输出流
	 * @param str
	 *            字符串
	 */
	public static void writeAll(Writer writer, String str) {
		try {
			writer.write(str);
			writer.flush();
		} catch (IOException e) {
			throw wrapThrow(e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					wrapThrow(e);
				}
			}
		}
	}

	/**
	 * 根据一段文本模拟出一个文本输入流对象
	 * 
	 * @param cs
	 *            文本
	 * @return 文本输出流对象
	 */
	public static Reader inr(CharSequence cs) {
		return new StringReader(cs.toString());
	}

	/**
	 * 较方便的创建一个数组，比如：
	 * 
	 * <pre>
	 * String[] strs = Lang.array("A", "B", "A"); => ["A","B","A"]
	 * </pre>
	 * 
	 * @param eles
	 *            可变参数
	 * @return 数组对象
	 */
	@SuppressWarnings("all")
	public static <T> T[] array(T... eles) {
		return eles;
	}

	/**
	 * 较方便的创建一个没有重复的数组，比如：
	 * 
	 * <pre>
	 * String[] strs = Lang.arrayUniq("A","B","A");  => ["A","B"]
	 * String[] strs = Lang.arrayUniq();  => null
	 * </pre>
	 * 
	 * 返回的顺序会遵循输入的顺序
	 * 
	 * @param eles
	 *            可变参数
	 * @return 数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayUniq(T... eles) {
		if (null == eles || eles.length == 0)
			return null;
		// 记录重复
		HashSet<T> set = new HashSet<T>(eles.length);
		for (T ele : eles) {
			set.add(ele);
		}
		// 循环
		T[] arr = (T[]) Array.newInstance(eles[0].getClass(), set.size());
		int index = 0;
		for (T ele : eles) {
			if (set.remove(ele))
				Array.set(arr, index++, ele);
		}
		return arr;

	}

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 * 
	 * @param obj
	 *            任意对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj.getClass().isArray())
			return Array.getLength(obj) == 0;
		if (obj instanceof Collection<?>)
			return ((Collection<?>) obj).isEmpty();
		if (obj instanceof Map<?, ?>)
			return ((Map<?, ?>) obj).isEmpty();
		return false;
	}

	/**
	 * 判断一个数组是否是空数组
	 * 
	 * @param ary
	 *            数组
	 * @return null 或者空数组都为 true 否则为 false
	 */
	public static <T> boolean isEmptyArray(T[] ary) {
		return null == ary || ary.length == 0;
	}

	/**
	 * 较方便的创建一个列表，比如：
	 * 
	 * <pre>
	 * List&lt;Pet&gt; pets = Lang.list(pet1, pet2, pet3);
	 * </pre>
	 * 
	 * 注，这里的 List，是 ArrayList 的实例
	 * 
	 * @param eles
	 *            可变参数
	 * @return 列表对象
	 */
	@SuppressWarnings("all")
	public static <T> ArrayList<T> list(T... eles) {
		ArrayList<T> list = new ArrayList<T>(eles.length);
		for (T ele : eles)
			list.add(ele);
		return list;
	}

	/**
	 * 创建一个 Hash 集合
	 * 
	 * @param eles
	 *            可变参数
	 * @return 集合对象
	 */
	@SuppressWarnings("all")
	public static <T> Set<T> set(T... eles) {
		Set<T> set = new HashSet<T>();
		for (T ele : eles)
			set.add(ele);
		return set;
	}

	/**
	 * 将多个数组，合并成一个数组。如果这些数组为空，则返回 null
	 * 
	 * @param arys
	 *            数组对象
	 * @return 合并后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[]... arys) {
		Queue<T> list = new LinkedList<T>();
		for (T[] ary : arys)
			if (null != ary)
				for (T e : ary)
					if (null != e)
						list.add(e);
		if (list.isEmpty())
			return null;
		Class<T> type = (Class<T>) list.peek().getClass();
		return list.toArray((T[]) Array.newInstance(type, list.size()));
	}

	/**
	 * 将一个对象添加成为一个数组的第一个元素，从而生成一个新的数组
	 * 
	 * @param e
	 *            对象
	 * @param eles
	 *            数组
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayFirst(T e, T[] eles) {
		try {
			if (null == eles || eles.length == 0) {
				T[] arr = (T[]) Array.newInstance(e.getClass(), 1);
				arr[0] = e;
				return arr;
			}
			T[] arr = (T[]) Array.newInstance(eles.getClass()
					.getComponentType(), eles.length + 1);
			arr[0] = e;
			for (int i = 0; i < eles.length; i++) {
				arr[i + 1] = eles[i];
			}
			return arr;
		} catch (NegativeArraySizeException e1) {
			throw wrapThrow(e1);
		}
	}

	/**
	 * 将一个对象添加成为一个数组的最后一个元素，从而生成一个新的数组
	 * 
	 * @param e
	 *            对象
	 * @param eles
	 *            数组
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayLast(T[] eles, T e) {
		try {
			if (null == eles || eles.length == 0) {
				T[] arr = (T[]) Array.newInstance(e.getClass(), 1);
				arr[0] = e;
				return arr;
			}
			T[] arr = (T[]) Array.newInstance(eles.getClass()
					.getComponentType(), eles.length + 1);
			for (int i = 0; i < eles.length; i++) {
				arr[i] = eles[i];
			}
			arr[eles.length] = e;
			return arr;
		} catch (NegativeArraySizeException e1) {
			throw wrapThrow(e1);
		}
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 * 
	 * @param fmt
	 *            格式
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concatBy(String fmt, T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T obj : objs)
			sb.append(String.format(fmt, obj));
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param ptn
	 *            格式
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concatBy(String ptn, Object c, T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T obj : objs)
			sb.append(String.format(ptn, obj)).append(c);
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, T[] objs) {
		StringBuilder sb = new StringBuilder();
		if (null == objs || 0 == objs.length)
			return sb;

		sb.append(objs[0]);
		for (int i = 1; i < objs.length; i++)
			sb.append(c).append(objs[i]);

		return sb;
	}

	/**
	 * 将一个长整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder concat(Object c, long[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(c).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder concat(Object c, int[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(c).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个数组的部分元素转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param offset
	 *            开始元素的下标
	 * @param len
	 *            元素数量
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(int offset, int len, Object c,
			T[] objs) {
		StringBuilder sb = new StringBuilder();
		if (null == objs || len < 0 || 0 == objs.length)
			return sb;

		if (offset < objs.length) {
			sb.append(objs[offset]);
			for (int i = 1; i < len && i + offset < objs.length; i++) {
				sb.append(c).append(objs[i + offset]);
			}
		}
		return sb;
	}

	/**
	 * 将一个数组所有元素拼合成一个字符串
	 * 
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T e : objs)
			sb.append(e.toString());
		return sb;
	}

	/**
	 * 将一个数组部分元素拼合成一个字符串
	 * 
	 * @param offset
	 *            开始元素的下标
	 * @param len
	 *            元素数量
	 * @param array
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(int offset, int len, T[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(array[i + offset].toString());
		}
		return sb;
	}

	/**
	 * 将一个集合转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, Collection<T> coll) {
		StringBuilder sb = new StringBuilder();
		if (null == coll || coll.isEmpty())
			return sb;
		return concat(c, coll.iterator());
	}

	/**
	 * 将一个迭代器转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, Iterator<T> it) {
		StringBuilder sb = new StringBuilder();
		if (it == null || !it.hasNext())
			return sb;
		sb.append(it.next());
		while (it.hasNext())
			sb.append(c).append(it.next());
		return sb;
	}

	/**
	 * 将一个或者多个数组填入一个集合。
	 * 
	 * @param <C>
	 *            集合类型
	 * @param <T>
	 *            数组元素类型
	 * @param coll
	 *            集合
	 * @param objss
	 *            数组 （数目可变）
	 * @return 集合对象
	 */
	@SuppressWarnings("all")
	public static <C extends Collection<T>, T> C fill(C coll, T[]... objss) {
		for (T[] objs : objss)
			for (T obj : objs)
				coll.add(obj);
		return coll;
	}
	@SuppressWarnings("all")
	private static <T extends Map<Object, Object>> Map<Object, Object> createMap(
			Class<T> mapClass) {
		Map<Object, Object> map;
		try {
			map = mapClass.newInstance();
		} catch (Exception e) {
			map = new HashMap<Object, Object>();
		}
		if (!mapClass.isAssignableFrom(map.getClass())) {
			throw makeThrow("Fail to create map [%s]", mapClass.getName());
		}
		return map;
	}

	/**
	 * 将数组转换成一个列表。
	 * 
	 * @param array
	 *            原始数组
	 * @return 新列表
	 * 
	 * @see org.nutz.castor.Castors
	 */
	public static <T> List<T> array2list(T[] array) {
		if (null == array)
			return null;
		List<T> re = new ArrayList<T>(array.length);
		for (T obj : array)
			re.add(obj);
		return re;
	}

	/**
	 * 安全的从一个数组获取一个元素，容忍 null 数组，以及支持负数的 index
	 * <p>
	 * 如果该下标越界，则返回 null
	 * 
	 * @param <T>
	 * @param array
	 *            数组，如果为 null 则直接返回 null
	 * @param index
	 *            下标，-1 表示倒数第一个， -2 表示倒数第二个，以此类推
	 * @return 数组元素
	 */
	public static <T> T get(T[] array, int index) {
		if (null == array)
			return null;
		int i = index < 0 ? array.length + index : index;
		if (i < 0 || i >= array.length)
			return null;
		return array[i];
	}

	/**
	 * 将字符串解析成 boolean 值，支持更多的字符串
	 * <ul>
	 * <li>1 | 0
	 * <li>yes | no
	 * <li>on | off
	 * <li>true | false
	 * </ul>
	 * 
	 * @param s
	 *            字符串
	 * @return 布尔值
	 */
	public static boolean parseBoolean(String s) {
		if (null == s || s.length() == 0)
			return false;
		if (s.length() > 5)
			return true;
		if ("0".equals(s))
			return false;
		s = s.toLowerCase();
		return !"false".equals(s) && !"off".equals(s) && !"no".equals(s);
	}

	/**
	 * 对Thread.sleep(long)的简单封装,不抛出任何异常
	 * 
	 * @param millisecond
	 *            休眠时间
	 */
	public static void quiteSleep(long millisecond) {
		try {
			if (millisecond > 0)
				Thread.sleep(millisecond);
		} catch (Throwable e) {
		}
	}

	/**
	 * 将字符串，变成数字对象，现支持的格式为：
	 * <ul>
	 * <li>null - 整数 0</li>
	 * <li>23.78 - 浮点 Float</li>
	 * <li>0x45 - 16进制整数 Integer</li>
	 * <li>78L - 长整数 Long</li>
	 * <li>69 - 普通整数 Integer</li>
	 * </ul>
	 * 
	 * @param s
	 *            参数
	 * @return 数字对象
	 */
	public static Number str2number(String s) {
		// null 值
		if (null == s) {
			return 0;
		}
		s = s.toUpperCase();
		// 浮点
		if (s.indexOf('.') != -1) {
			char c = s.charAt(s.length() - 1);
			if (c == 'F' || c == 'f') {
				return Float.valueOf(s);
			}
			return Double.valueOf(s);
		}
		// 16进制整数
		if (s.startsWith("0X")) {
			return Integer.valueOf(s.substring(2), 16);
		}
		// 长整数
		if (s.charAt(s.length() - 1) == 'L' || s.charAt(s.length() - 1) == 'l') {
			return Long.valueOf(s.substring(0, s.length() - 1));
		}
		// 普通整数
		Long re = Long.parseLong(s);
		if (Integer.MAX_VALUE >= re && re >= Integer.MIN_VALUE)
			return re.intValue();
		return re;
	}

	/**
	 * 返回一个集合对象的枚举对象。实际上就是对 Iterator 接口的一个封装
	 * 
	 * @param col
	 *            集合对象
	 * @return 枚举对象
	 */
	public static <T> Enumeration<T> enumeration(Collection<T> col) {
		final Iterator<T> it = col.iterator();
		return new Enumeration<T>() {
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			public T nextElement() {
				return it.next();
			}
		};
	}

	/**
	 * 将枚举对象，变成集合
	 * 
	 * @param enums
	 *            枚举对象
	 * @param cols
	 *            集合对象
	 * @return 集合对象
	 */
	public static <T extends Collection<E>, E> T enum2collection(
			Enumeration<E> enums, T cols) {
		while (enums.hasMoreElements())
			cols.add(enums.nextElement());
		return cols;
	}

	/**
	 * 将字符数组强制转换成字节数组。如果字符为双字节编码，则会丢失信息
	 * 
	 * @param cs
	 *            字符数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(char[] cs) {
		byte[] bs = new byte[cs.length];
		for (int i = 0; i < cs.length; i++)
			bs[i] = (byte) cs[i];
		return bs;
	}

	/**
	 * 将整数数组强制转换成字节数组。整数的高位将会被丢失
	 * 
	 * @param is
	 *            整数数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(int[] is) {
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; i++)
			bs[i] = (byte) is[i];
		return bs;
	}

	/**
	 * 判断当前系统是否为Windows
	 * 
	 * @return true 如果当前系统为Windows系统
	 */
	public static boolean isWin() {
		try {
			String os = System.getenv("OS");
			return os != null && os.indexOf("Windows") > -1;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * 使用当前线程的ClassLoader加载给定的类
	 * 
	 * @param className
	 *            类的全称
	 * @return 给定的类
	 * @throws ClassNotFoundException
	 *             如果无法用当前线程的ClassLoader加载
	 */
	public static Class<?> loadClass(String className)
			throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader()
					.loadClass(className);
		} catch (ClassNotFoundException e) {
			return Class.forName(className);
		}
	}

	/**
	 * 获取基本类型的默认值
	 * 
	 * @param pClass
	 *            基本类型
	 * @return 0/false,如果传入的pClass不是基本类型的类,则返回null
	 */
	public static Object getPrimitiveDefaultValue(Class<?> pClass) {
		if (int.class.equals(pClass))
			return Integer.valueOf(0);
		if (long.class.equals(pClass))
			return Long.valueOf(0);
		if (short.class.equals(pClass))
			return Short.valueOf((short) 0);
		if (float.class.equals(pClass))
			return Float.valueOf(0f);
		if (double.class.equals(pClass))
			return Double.valueOf(0);
		if (byte.class.equals(pClass))
			return Byte.valueOf((byte) 0);
		if (char.class.equals(pClass))
			return Character.valueOf((char) 0);
		if (boolean.class.equals(pClass))
			return Boolean.FALSE;
		return null;
	}

	/**
	 * 获取一个 Type 类型实际对应的Class
	 * 
	 * @param type
	 *            类型
	 * @return 与Type类型实际对应的Class
	 */
	@SuppressWarnings("rawtypes")
	public static Class<?> getTypeClass(Type type) {
		Class<?> clazz = null;
		if (type instanceof Class<?>) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			clazz = (Class<?>) pt.getRawType();
		} else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			Class<?> typeClass = getTypeClass(gat.getGenericComponentType());
			return Array.newInstance(typeClass, 0).getClass();
		} else if (type instanceof TypeVariable) {
			TypeVariable tv = (TypeVariable) type;
			Type[] ts = tv.getBounds();
			if (ts != null && ts.length > 0)
				return getTypeClass(ts[0]);
		} else if (type instanceof WildcardType) {
			WildcardType wt = (WildcardType) type;
			Type[] t_low = wt.getLowerBounds();// 取其下界
			if (t_low.length > 0)
				return getTypeClass(t_low[0]);
			Type[] t_up = wt.getUpperBounds(); // 没有下界?取其上界
			return getTypeClass(t_up[0]);// 最起码有Object作为上界
		}
		return clazz;
	}

	/**
	 * 返回一个 Type 的泛型数组, 如果没有, 则直接返回null
	 * 
	 * @param type
	 *            类型
	 * @return 一个 Type 的泛型数组, 如果没有, 则直接返回null
	 */
	public static Type[] getGenericsTypes(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			return pt.getActualTypeArguments();
		}
		return null;
	}

	/**
	 * 强制从字符串转换成一个 Class，将 ClassNotFoundException 包裹成 RuntimeException
	 * 
	 * @param <T>
	 * @param name
	 *            类名
	 * @param type
	 *            这个类型的边界
	 * @return 类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String name, Class<T> type) {
		Class<?> re;
		try {
			re = Class.forName(name);
			return (Class<T>) re;
		} catch (ClassNotFoundException e) {
			throw wrapThrow(e);
		}
	}

	/**
	 * 获取指定输入流的 MD5 值
	 * 
	 * @param ins
	 *            输入流
	 * @return 指定输入流的 MD5 值
	 * @see #digest(String, InputStream)
	 */
	public static String md5(InputStream ins) {
		return digest("MD5", ins);
	}

	/**
	 * 获取指定输入流的 SHA1 值
	 * 
	 * @param ins
	 *            输入流
	 * @return 指定输入流的 SHA1 值
	 * @see #digest(String, InputStream)
	 */
	public static String sha1(InputStream ins) {
		return digest("SHA1", ins);
	}

	/**
	 * 从流计算出数字签名，计算完毕流会被关闭
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param ins
	 *            输入流
	 * @return 数字签名
	 */
	public static String digest(String algorithm, InputStream ins) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);

			byte[] bs = new byte[1024];
			int len = 0;
			while ((len = ins.read(bs)) != -1) {
				md.update(bs, 0, len);
			}

			byte[] hashBytes = md.digest();

			return fixedHexString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw wrapThrow(e);
		} catch (FileNotFoundException e) {
			throw wrapThrow(e);
		} catch (IOException e) {
			throw wrapThrow(e);
		} finally {
			if (null != ins) {
				try {
					ins.close();
				} catch (IOException e) {
					throw wrapThrow(e);
				}
			}
		}
	}

	/**
	 * 从字节数组计算出数字签名
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param bytes
	 *            字节数组
	 * @param salt
	 *            随机字节数组
	 * @param iterations
	 *            迭代次数
	 * @return 数字签名
	 */
	public static String digest(String algorithm, byte[] bytes, byte[] salt,
			int iterations) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				md.update(salt);
			}

			byte[] hashBytes = md.digest(bytes);

			for (int i = 1; i < iterations; i++) {
				md.reset();
				hashBytes = md.digest(hashBytes);
			}

			return fixedHexString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw wrapThrow(e);
		}
	}

	/** 当前运行的 Java 虚拟机是否是在安卓环境 */
	public static final boolean isAndroid;
	static {
		boolean flag = false;
		try {
			Class.forName("android.Manifest");
			flag = true;
		} catch (Throwable e) {
		}
		isAndroid = flag;
	}

	/**
	 * 将指定的数组的内容倒序排序。注意，这会破坏原数组的内容
	 * 
	 * @param arrays
	 *            指定的数组
	 */
	public static <T> void reverse(T[] arrays) {
		int size = arrays.length;
		for (int i = 0; i < size; i++) {
			int ih = i;
			int it = size - 1 - i;
			if (ih == it || ih > it) {
				break;
			}
			T ah = arrays[ih];
			T swap = arrays[it];
			arrays[ih] = swap;
			arrays[it] = ah;
		}
	}

	public static String simpleMetodDesc(Method method) {
		return String.format("%s.%s(...)", method.getDeclaringClass()
				.getSimpleName(), method.getName());
	}

	public static String fixedHexString(byte[] hashBytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashBytes.length; i++) {
			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

	/**
	 * 一个便利的方法，将当前线程睡眠一段时间
	 * 
	 * @param ms
	 *            要睡眠的时间 ms
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw wrapThrow(e);
		}
	}

	/**
	 * 一个便利的等待方法同步一个对象
	 * 
	 * @param lock
	 *            锁对象
	 * @param ms
	 *            要等待的时间 ms
	 */
	public static void wait(Object lock, long ms) {
		if (null != lock)
			synchronized (lock) {
				try {
					lock.wait(ms);
				} catch (InterruptedException e) {
					throw wrapThrow(e);
				}
			}
	}

	/**
	 * 通知对象的同步锁
	 * 
	 * @param lock
	 *            锁对象
	 */
	public static void notifyAll(Object lock) {
		if (null != lock)
			synchronized (lock) {
				lock.notifyAll();
			}
	}
}
