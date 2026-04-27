package vMachine_v3.view;

import vMachine_v3.dto.MemberDto;
import vMachine_v3.dto.SalesDto;
import vMachine_v3.service.DrinkService;
import vMachine_v3.service.MemberService;
import vMachine_v3.service.SalesService;

import java.util.List;
import java.util.Scanner;

public class MemberView {
    private final Scanner scanner =  new Scanner(System.in);
    private final MemberService memberService = new MemberService();
    private final DrinkService drinkService = new DrinkService();
    private final SalesService salesService = new SalesService();
    private final DrinkView drinkView = new DrinkView();
    private final AdminView adminView = new AdminView();

    public void start() {
        while (true) {
            System.out.println("============================================");
            System.out.println(" 자판기에 오신걸 환영합니다.");
            System.out.println("============================================");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            System.out.print("번호를 선택해 주세요. : ");

            int menu = readInt();
            switch (menu) {
                case 1 :
                    register();
                    break;
                case 2 :
                    login();
                    break;
                case 3 :
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default :
                    System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void register() {
        MemberDto dto = new MemberDto();
        System.out.print("아이디 : ");
        dto.setUserId(scanner.nextLine());
        System.out.print("비밀번호 : ");
        dto.setPassword(scanner.nextLine());
        System.out.print("이름 : ");
        dto.setName(scanner.nextLine());
        System.out.print("전화번호 : ");
        dto.setTel(scanner.nextLine());
        System.out.print("카드번호 : ");
        dto.setCardNum(scanner.nextLine());
        dto.setIsAdmin(0);

        boolean result = memberService.register(dto);
        if (result) {
            System.out.println("회원가입 성공!");
        } else {
            System.out.println("회원가입 실패");
        }
    }

    private void login() {
        System.out.println("아이디 : ");
        String userId = scanner.nextLine();
        System.out.println("비밀번호 : ");
        String pw  = scanner.nextLine();

        MemberDto loginUser = memberService.login(userId, pw);
        if (loginUser == null) {
            System.out.println("로그인 실패!");
            return;
        }
        if (loginUser.getIsAdmin() == 1) {
            adminView.start(loginUser);
        } else {
            userMenu(loginUser);
            return;
        }
    }

    private void userMenu(MemberDto loginUser) {
        while (true) {
            MemberDto fresh = memberService.getById(loginUser.getId());
            if (fresh == null) {
                return;
            }
            loginUser = fresh;

            System.out.println("============================================");
            System.out.println(" 안녕하세요, ["+loginUser.getName()+"]님! 잔액 : ["+loginUser.getBalance()+"]");
            System.out.println("============================================");
            System.out.println("1. 메뉴보기");
            System.out.println("2. 음료 구매");
            System.out.println("3. 금액 충전");
            System.out.println("4. 구매 내역");
            System.out.println("5. 로그아웃");
            System.out.println("번호를 선택해 주세요. : ");

            int menu = readInt();
            switch (menu) {
                case 1:
                    drinkView.printMenuList(drinkService.getAll());
                    break;
                case 2:
                    buyDrink(loginUser.getId());
                    break;
                case 3:
                    charge(loginUser.getId());
                    break;
                case 4:
                    showHistory(loginUser.getId());
                    break;
                case 5:
                    System.out.println("로그아웃 되었습니다.");
                    return;
                default:
                    System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    public void buyDrink(int memberId) {
        drinkView.printMenuList(drinkService.getAll());
        System.out.println("구매할 메뉴 ID: ");
        int menuId = scanner.nextInt();

        int result = drinkService.sell(memberId, menuId);
        if (result == 1) {
            System.out.println("구매성공!");
        } else if (result == -1) {
            System.out.println("존재하지 않는 메뉴입니다.");
        } else if (result == -2) {
            System.out.println("품절된 상품입니다.");
        } else if (result == -3) {
            System.out.println("잔액 부족합니다");
        } else if (result == -4) {
            System.out.println("회원 정보가 없습니다.");
        } else {
            System.out.println("구매 실패");
        }
    }

    private void charge(int memberId) {
        System.out.println("충전 금액(1000원 단위) : ");
        int amount = readInt();

        int result = memberService.charge(memberId, amount);
        if (result > 0) {
            System.out.println("충전 성공");
        } else {
            System.out.println("충전 실패(1000원 단위, 음수 입력 불가) : ");
        }
    }

    public void showHistory(int memberId) {
        List<SalesDto> list = salesService.getByMember(memberId);
        int sum = 0;

        System.out.println("구매 일시         제품명         금액");
        System.out.println("----------------------------------");

        for (SalesDto dto : list) {
            String soldAt = "-";
            if (dto.getSoldAt() != null) {
                soldAt = dto.getSoldAt().toString().replace("T", "");
                if (soldAt.length() > 16) {
                    soldAt = soldAt.substring(0, 16);
                }
            }
            System.out.println(soldAt+"    "+dto.getMenuName()+ "   "+dto.getPrice()+"원");
            sum += dto.getPrice();
        }
        System.out.println("----------------------------------");
        System.out.println("총 구매금액: " + sum + "원");
    }

    private int readInt() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            }  catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("숫자를 입력해 주세요 : ");
            }
        }
    }
}
