package ftps;

public class CommandFactory {

	public static Command createCommand(String type) {
		String result=CommandType.USER.getValue();
		switch(type)
		{
			case "":new UserCommand();
			
			default :return null;
		}
		
	}
}
