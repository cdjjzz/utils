package ftps;

public class CommandFactory {

	public static Command createCommand(String name) {
		try {
			CommandType type=Enum.valueOf(CommandType.class, name.toUpperCase());
			switch(type)
			{
				case USER:return new UserCommand();
				
				case PASS:return new PassCommand();
				
				case QUIT:return new QuitCommand();
				
				case CWD:return new CwdCommand();
				
				case LS:return new DirCommand();
				
				case LIST:return new DirCommand();
				
				case GET:return new GetCommand();
				
				case PORT:return new PortCommand();
				
				case STOR:return new StoreCommand();
				
				default :return null;
			}
		} catch (Exception e) {
			    return null;
		}
		
		
	}
}
