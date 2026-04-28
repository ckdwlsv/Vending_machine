package vMachine_v3.view;

import vMachine_v3.dto.DrinkDto;
import vMachine_v3.dto.MemberDto;
import vMachine_v3.dto.SalesDto;
import vMachine_v3.service.DrinkService;
import vMachine_v3.service.MemberService;
import vMachine_v3.service.SalesService;

import java.util.List;
import java.util.Scanner;

public class AdminView {
    private final Scanner sc =  new Scanner(System.in);
    private final DrinkService drinkService = new DrinkService();
    private final MemberService memberService = new MemberService();
    private final SalesService salesService = new SalesService();
    private final DrinkView drinkView = new DrinkView();

    public void start(MemberDto loginUser) {

        while(true) {
            MemberDto fresh = memberService.getById(loginUser.getId());

            System.out.println("=============================");
            System.out.println("관리자 메뉴");
            System.out.println("=============================");
            System.out.println("1. 자판기 관리");
            System.out.println("2. 회원 관리");
            System.out.println("3. 판매 관리");
            System.out.println("4. 로그아웃");
            System.out.print("번호를 선택해 주세요. : ");

            int menu = readInt();
            if (menu == 1) {
                manageDrink();
            } else if (menu == 2) {
                manageMember();
            } else if (menu == 3) {
                manageSales();
            } else if (menu == 4) {
                return;
            } else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void manageDrink() {
        System.out.println("1. 메뉴 추가");
        System.out.println("2. 메뉴 수정");
        System.out.println("3. 메뉴 삭제");
        System.out.println("4. 전체 조회");
        System.out.print("번호를 선택해 주세요. : ");
        int menu = readInt();

        if (menu == 1) {
            DrinkDto dto = new DrinkDto();
            System.out.println("제품명 : ");
            dto.setName(sc.next());
            System.out.println("가격 : ");
            dto.setPrice(sc.nextInt());
            System.out.println("재고 : ");
            dto.setStock(sc.nextInt());
            int result = drinkService.insert(dto);
            System.out.println(result > 0 ? "추가 성공" : "추가 실패");
        } else if (menu == 2) {
            DrinkDto dto = new DrinkDto();
            System.out.println("수정할 ID : ");
            dto.setId(readInt());
            System.out.println("제품명 : ");
            dto.setName(sc.next());
            System.out.println("가격 : ");
            dto.setPrice(sc.nextInt());
            System.out.println("재고 : ");
            dto.setStock(sc.nextInt());
            int result = drinkService.update(dto);
            System.out.println(result >0 ? "수정 성공" : "수정 실패");
        } else if (menu == 3) {
            System.out.println("삭제할 ID : ");
            int id = readInt();
            int result = drinkService.delete(id);
            System.out.println(result > 0 ? "삭제 성공" : "삭제 실패");
        } else if (menu == 4) {
            drinkView.printMenuList(drinkService.getAll());
        } else {
            System.out.println("잘못된 메뉴입니다.");
        }
    }

    private void manageMember() {
        System.out.println("1. 회원 추가");
        System.out.println("2. 회원 수정");
        System.out.println("3. 회원 삭제");
        System.out.println("4. 전체 조회");
        System.out.print("번호를 선택해 주세요. : ");
        int menu = readInt();

        if (menu == 1) {
            MemberDto dto = inputMember(1);
            boolean result = memberService.register(dto);
            if (result) {
                System.out.println("추가 성공");
            } else {
                System.out.println("추가 실패(중복아이디/카드유효성 확인)");
            }
        } else if (menu == 2) {
            System.out.println("수정할 회원 ID : ");
            int id = readInt();
            MemberDto dto = inputMember(-1);
            dto.setId(id);
            int result = memberService.update(dto);
            System.out.println(result > 0 ? "수정 성공" : "수정 실패");
        } else if (menu == 3) {
            System.out.println("삭제할 회원 ID : ");
            int id = readInt();
            int result = memberService.delete(id);
            System.out.println(result > 0 ? "삭제 성공" : "삭제 실패");
        } else if (menu == 4) {
            printMembers(memberService.getAll());
        } else {
            System.out.println("잘못된 메뉴입니다.");
        }
    }

    private void printMembers(List<MemberDto> list) {
        System.out.println("ID    아이디    이름     전화번호     잔액    권한");
        System.out.println("----------------------------------------------");
        for (MemberDto dto : list) {
            String role = dto.getIsAdmin() == 1 ? "관리자" : "일반";
            System.out.println(dto.getId() + " "+dto.getUserId()+" "+dto.getName()+" "+dto.getTel()+" "+dto.getBalance()+" "+role);
        }
    }

    private MemberDto inputMember(int defaultAdmin) {
        MemberDto dto = new MemberDto();
        System.out.println("아이디 : ");
        dto.setUserId(sc.nextLine());
        System.out.println("비밀번호 : ");
        dto.setPassword(sc.nextLine());
        System.out.println("이름 : ");
        dto.setName(sc.nextLine());
        System.out.println("전화번호 : ");
        dto.setTel(sc.nextLine());
        System.out.println("카드번호 : ");
        dto.setCardNum(sc.nextLine());

        if (defaultAdmin == 0) {
            dto.setIsAdmin(defaultAdmin);
        } else {
            System.out.println("관리자 여부(0:일반, 1:관리자) : ");
            dto.setIsAdmin(readInt());
        }
        return dto;
    }

    private void manageSales() {
        System.out.println("1. 제품별 판매 현황");
        System.out.println("2. 회원별 구매 현황");
        System.out.print("번호를 선택해 주세요. : ");
        int menu = readInt();

        if (menu == 1) {
            List<SalesDto> list = salesService.getSummaryByMenu();
            int totalQty = 0;
            int totalAmount = 0;
            System.out.println("제품명      판매수량    판매금액");
            System.out.println("-----------------------------");
            for (SalesDto dto : list) {
                System.out.println(dto.getMenuName()+" "+dto.getTotalQuantity()+"개 "+dto.getTotalAmount()+"원");
                totalQty += dto.getTotalQuantity();
                totalAmount += dto.getTotalAmount();
            }
            System.out.println("-----------------------------");
            System.out.println("합계 : "+totalQty+"개 "+totalAmount+"원");
        } else if (menu == 2) {
            List<SalesDto> list = salesService.getSummaryByMember();
            System.out.println("아이디    회원명    구매금액    충전잔액");
            System.out.println("-----------------------------------");
            for (SalesDto dto : list) {
                System.out.println(dto.getUserId()+" "+dto.getMemberName()+" "+dto.getTotalAmount()+"원 "+dto.getBalance()+"원");
            }
        } else {
            System.out.println("잘못된 메뉴입니다.");
        }
    }

    private int readInt() {
        while (true) {
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            }  catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                System.out.println("숫자를 입력하세요. : ");
            }
        }
    }
}
