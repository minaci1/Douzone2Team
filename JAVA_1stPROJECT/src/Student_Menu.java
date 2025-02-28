import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Student_Menu extends Menu {


	public String fileName = "test.txt";
	public List<Account> accounts = new ArrayList<>();
	public String loginID="";

	public void signUp() {
		load(fileName); //기존 id 정보 확인하기 위하여 load
		
		String tmp="";
		String getAccountId = "";
		String getPassWord = "";
		String getName = "";
		String getPhoneNumber = "";
		int getClassNumber = 0;

		String pwdPattern = "^(?=.*[A-Za-z])(?=.*\\d)([!@#$%*?&]?)[A-Za-z\\d!@#$%*?&]{10,16}$"; // 비밀번호 형식
		String phoneNumberPattern = "^01[0-1|6-9]\\-?\\d{3,4}\\-?\\d{4}$"; // 핸드폰번호 형식
		String idPattern = "^.+@[^\\\\.].*\\\\.[a-z]{2,}$"; // 이메일 형식
		Scanner sc = new Scanner(System.in);
		int choice = 0;

		boolean run = true;
		while (run) {

			switch (choice) {

			case 0:
				System.out.print("이름을 입력해주세요 :");
				getName = sc.nextLine();
				choice++;
				break;
			case 1:
				System.out.print("회원가입 하실 이메일 주소를 입력해주세요 (example@gmail.com) :");
				tmp = sc.nextLine();
				boolean idregex = Pattern.matches(idPattern, tmp);

				if (idregex) { // 이메일 형식이 아닐 경우
					System.out.print("이메일 형식이 잘못되었습니다.다시 입력해주세요");
				} else {// false면 통과
					System.out.println("##맵##" + map.containsKey(tmp));
					if(!map.containsKey(tmp)) { //다르면 사용 가능하다.
						System.out.println("사용 가능한 ID 입니다.");
                        getAccountId = tmp;
						choice++;
					}else {
						System.out.println("ID가 존재합니다. 다른 ID로 입력해주세요.");
					}
				}
                break;
			case 2:
				System.out.print("비밀번호를 입력해주세요 (10~16자리 사이의 영문자,숫자,특수문자가 포함) : ");
				tmp = sc.nextLine();
				boolean pwdregex = Pattern.matches(pwdPattern, tmp);

				if (!pwdregex) {
					System.out.print("비밀번호 형식이 잘못되었습니다. 다시 입력해주세요");
				} else {
					System.out.print("정상적으로 입력하였습니다.");
					getPassWord = tmp;
					choice++;
				}
				break;
			case 3:
				System.out.print("핸드폰 번호를 입력해주세요 (010-0000-0000) :");
				tmp = sc.nextLine();
				boolean phoneregex = Pattern.matches(phoneNumberPattern, getPhoneNumber);

				if (phoneregex) {
					System.out.print("핸드폰 번호 형식이 잘못되었습니다. 다시 입력해주세요");
				} else {
					System.out.print("정상적으로 입력하였습니다.");
					getPhoneNumber = tmp;
					choice++;
				}
				break;
			case 4:
				try {
					System.out.print("반번호를 입력해주세요(1:(더존) or 2:(현대) 중 번호 선택");
					getClassNumber = sc.nextInt();
				} catch (NumberFormatException e) {
					System.out.println("숫자가 아닌 문자로 입력하셨습니다.");
					System.out.print("1과 2 중 선택 가능합니다.");
					break;
				} 
				
				if ((getClassNumber == 1 || getClassNumber == 2)) { // 다시 try 구문으로 가기 로직 다시
					System.out.print("모든 정보가 정상적으로 입력되었습니다.");
					Account acc = new Account(getName, getAccountId, getPassWord, getPhoneNumber, getClassNumber);
					accounts.add(acc); // 회원정보 ArrayList 생성
					map.put(getAccountId, acc); // ArrayList에 생성된 정보 키 :id / 나머지 정보 : 값으로 생성
					System.out.println("******" + map.get(getAccountId).getName());
					save(map, fileName);
					run = false;
				} 
				break;
			}
		}

	}

	
	public void attendance(int flag)  {
		String path = "./Attendance/attendance.txt";
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy.MM.dd a HH:mm:ss");
		String data = "";
		int ntmp = map.get(loginId).getClassNumber();
		if( flag == 1 ) data = loginId + " " + map.get(loginId).getName()+ " " + (ntmp == 1?"더존반":"현대반") + " 출근 "+(String)now.format(date);
		else data  = loginId + " " + map.get(loginId).getName()+ " " + (ntmp == 1?"더존반":"현대반")+ " 퇴근 "+(String)now.format(date);
		
		try {
			writelist(path, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		if( flag == 1 ) System.out.println("출석이 완료 되었습니다.");
		else System.out.println("퇴근이 완료 되었습니다.");
	}


	@Override
	void MenuRun()  {
			
		Scanner sc = new Scanner(System.in);
 
		boolean run = false;
		while (!run) {
			System.out.println("********************************************");
			System.out.println("************ Douzone In and Out ************");
			System.out.println("********************************************");
			System.out.print("[1]로그인  [2]회원가입  [0]종료  : ");
			int select = Integer.parseInt(sc.nextLine());

			switch (select) {
			case 0:
				System.exit(0);
				break;
			case 1:
				if(login()) {
					menu2(sc);
					run = true;
				}
				else System.out.println("로그인이 실패하였습니다. 처음으로 돌아갑니다.");
				break;
			case 2:
				signUp();
				break;

			}
		}

	}

	public void menu2(Scanner sc)  {
		// 로그인 성공 시
		boolean flag = true;
		while(flag) {
	
			System.out.println("[1]출석  [2]퇴근");
			System.out.println("메뉴를 선택해주세요.");
	
			int select = Integer.parseInt(sc.nextLine());
	
			switch (select) {
				case 1:
					attendance(1);
					flag = false;
					break;
				case 2:
					attendance(2);
					flag = false;
					break;
				default:
			        System.out.println("잘 못 입력하였습니다.");
					break;
			}
		}
	}
	
	@Override
	void checkAttendance() {
	
	}
}
/*   
// 출결확인
@Override
void checkAttendance() {
	Scanner sc = new Scanner(System.in);
	boolean run = false;

	while (!run) {
		System.out.print("[1]나의근태 현황  [2]그룹근태 현황  [0]돌아가기  :");
		int menu = Integer.parseInt(sc.nextLine());
		System.out.println();

		switch (menu) {
		case 1:
			// 나의 근태 현황 가져오기~ (전부)
			break;

		case 2:
			// 같은 반 학생들의 근태 현황 가져오기~ (당일)
			break;

		case 0:
			run = true;
			break;

		default:
			System.out.println("잘못된 메뉴를 선택하셨습니다.");
		}
	}
}

@Override
void editInfo() {
	Scanner sc = new Scanner(System.in);

	String password = "";
	boolean run = false;
	while (!run) {
		if (password.equals("")) {
			System.out.print("비밀번호를 입력하세요: ");
			password = sc.nextLine();
			System.out.println();

			if (!map.get(loginId).getPassWord().equals(password)) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				password = "";
			}
		} else {
			System.out.print("메뉴를 선택하세요: [1]휴대폰 번호 변경  [2]비밀번호 변경  [0]이전 메뉴로 돌아가기 : ");
			int menu = Integer.parseInt(sc.nextLine());
			System.out.println();

			switch (menu) {
			case 1:
				// 휴대폰 번호 변경
				System.out.print("변경할 휴대폰 번호를 입력하세요: ");
				String phoneNumber = sc.nextLine();
				System.out.println();
				if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", phoneNumber)) {
					System.out.println("휴대폰 번호 형식에 맞지 않습니다.");
				} else {
					//getName, getAccountId, getPassWord, getPhoneNumber, getClassNumber
					map.replace(loginId, new Account(map.get(loginId).getName(), loginId, map.get(loginId).getPassWord(), phoneNumber, map.get(loginId).getClassNumber()));
				}
				break;

			case 2:
				// 비밀번호 변경
				System.out.print("새 비밀번호를 입력하세요: \n글자 수 제한 10~16자\n(영문자, 숫자 포함, 특수문자 포함)");
				String newPassword = sc.nextLine();
				System.out.println();
				if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)([!@#$%*?&]?)[A-Za-z\\d!@#$%*?&]{10,16}$",
						newPassword)) {
					System.out.println("입력하신 비밀번호가 양식에 맞지 않습니다.");
				} else {
					System.out.print("비밀번호를 한번 더 입력하세요: ");
					String newPasswordCheck = sc.nextLine();
					System.out.println();

					// 비밀번호가 일치 안 하는 경우
					if (!newPassword.equals(newPasswordCheck)) {
						System.out.println("비밀번호가 일치하지 않습니다.");
					} else {
						System.out.println("비밀번호가 변경되었습니다.");
						map.replace(loginId, new Account(map.get(loginId).getName(), loginId, newPassword, map.get(loginId).getPhoneNumber(), map.get(loginId).getClassNumber()));
					}
				}
				break;

			case 0:
				run = true;
				break;

			default:
				System.out.println("잘못된 메뉴를 선택하셨습니다.");
			}
		}
	}
}
*/
