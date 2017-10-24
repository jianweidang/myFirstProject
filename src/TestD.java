import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class TestD {
	private static final String PRINTER_DLL = "CDFPSK.dll";

	// 先将DLL文件拷到C盘下的 System32的目录下
	// 静态 加载dll 文件
	static {

		// 这里有三种方法 这里其中一种
		System.loadLibrary("CDFPSK");

	}

	public static void main(String[] args) {
		TestD tdll = new TestD();
		// String path = WINPSK.class.getResource("/").getPath();

		JNative jnative = null;

		try {
			jnative = tdll.getJNativeByFunction("OpenPort"); // 初始化打印机
			jnative.setRetVal(Type.INT); // 设置返回值类型
			jnative.setParameter(0, Type.STRING, "POSTEK G2000"); // 参数的 序号 类型
																	// 参数值
			jnative.invoke();
			Integer isready = Integer.parseInt(jnative.getRetVal()); // 判断打印机是否准备就绪
			System.out.println("isready=" + isready);
			if (isready == 1) {
				System.out.println("条码机可以开始接收资料!Starting...");
				jnative = tdll.getJNativeByFunction("PTK_DrawBar2D_QR");
				jnative.setRetVal(Type.INT);
				jnative.setParameter(0, Type.INT, "0"); // X 座标
				jnative.setParameter(1, Type.INT, "0"); // Y 座标。备注：1 dot = 0.125
														// mm。
				jnative.setParameter(2, Type.INT, "27"); // 最大列印宽度，单位 dots。
				jnative.setParameter(3, Type.INT, "27"); // 最大列印高度，单位 dots
				jnative.setParameter(4, Type.INT, "0"); // 设置旋转方向, 范围：0～3。
				jnative.setParameter(5, Type.INT, "1"); // 设置放大倍数，以点(dots)为单位,范围值：(1
														// - 9)。
				jnative.setParameter(6, Type.INT, "4"); // QR码编码模式选择,范围值(0 - 4)。
				jnative.setParameter(7, Type.INT, "1"); // QR码纠错等级选择,范围值(0 - 3)。
				jnative.setParameter(8, Type.INT, "8"); // QR码纠错等级选择,范围值(0 - 3)。
				jnative.setParameter(9, Type.STRING,
						"00000000000000000000000 0000 id1234568796"); // 资料字串。

				jnative.invoke();
			} else {
				System.out.println("打印机无法接受资料");
			}

		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (jnative != null) {
				try {
					// 关闭
					jnative = tdll.getJNativeByFunction("ClosePort");
					// jNative.setParameter(0, Type.INT, "true");
					jnative.invoke();
					jnative.dispose();
					System.out.println("end...打印完成!");
				} catch (NativeException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 通过方法名取得JNative对象
	public JNative getJNativeByFunction(String functionName)
			throws NativeException {
		return new JNative(PRINTER_DLL, functionName);
	}

}
