import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class TestD {
	private static final String PRINTER_DLL = "CDFPSK.dll";

	// �Ƚ�DLL�ļ�����C���µ� System32��Ŀ¼��
	// ��̬ ����dll �ļ�
	static {

		// ���������ַ��� ��������һ��
		System.loadLibrary("CDFPSK");

	}

	public static void main(String[] args) {
		TestD tdll = new TestD();
		// String path = WINPSK.class.getResource("/").getPath();

		JNative jnative = null;

		try {
			jnative = tdll.getJNativeByFunction("OpenPort"); // ��ʼ����ӡ��
			jnative.setRetVal(Type.INT); // ���÷���ֵ����
			jnative.setParameter(0, Type.STRING, "POSTEK G2000"); // ������ ��� ����
																	// ����ֵ
			jnative.invoke();
			Integer isready = Integer.parseInt(jnative.getRetVal()); // �жϴ�ӡ���Ƿ�׼������
			System.out.println("isready=" + isready);
			if (isready == 1) {
				System.out.println("��������Կ�ʼ��������!Starting...");
				jnative = tdll.getJNativeByFunction("PTK_DrawBar2D_QR");
				jnative.setRetVal(Type.INT);
				jnative.setParameter(0, Type.INT, "0"); // X ����
				jnative.setParameter(1, Type.INT, "0"); // Y ���ꡣ��ע��1 dot = 0.125
														// mm��
				jnative.setParameter(2, Type.INT, "27"); // �����ӡ��ȣ���λ dots��
				jnative.setParameter(3, Type.INT, "27"); // �����ӡ�߶ȣ���λ dots
				jnative.setParameter(4, Type.INT, "0"); // ������ת����, ��Χ��0��3��
				jnative.setParameter(5, Type.INT, "1"); // ���÷Ŵ������Ե�(dots)Ϊ��λ,��Χֵ��(1
														// - 9)��
				jnative.setParameter(6, Type.INT, "4"); // QR�����ģʽѡ��,��Χֵ(0 - 4)��
				jnative.setParameter(7, Type.INT, "1"); // QR�����ȼ�ѡ��,��Χֵ(0 - 3)��
				jnative.setParameter(8, Type.INT, "8"); // QR�����ȼ�ѡ��,��Χֵ(0 - 3)��
				jnative.setParameter(9, Type.STRING,
						"00000000000000000000000 0000 id1234568796"); // �����ִ���

				jnative.invoke();
			} else {
				System.out.println("��ӡ���޷���������");
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
					// �ر�
					jnative = tdll.getJNativeByFunction("ClosePort");
					// jNative.setParameter(0, Type.INT, "true");
					jnative.invoke();
					jnative.dispose();
					System.out.println("end...��ӡ���!");
				} catch (NativeException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// ͨ��������ȡ��JNative����
	public JNative getJNativeByFunction(String functionName)
			throws NativeException {
		return new JNative(PRINTER_DLL, functionName);
	}

}
