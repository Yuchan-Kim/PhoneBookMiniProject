package com.javaex.phonebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PhoneBookApp {

    public static void main(String[] args) {

        // 시작 화면 문구 출력
        System.out.println("*************************************************");
        System.out.println("*\t\t 전화번호 관리 프로그램 \t\t*");
        System.out.println("*************************************************");
        System.out.println();

        // 목록 리스트로 받기 위한 변수 선언
        List<Person> pList = new ArrayList<>();

        // 파일 읽기 스트림 생성
        try (FileInputStream in = new FileInputStream("/Users/yuchan/eclipse-workspace/Project_PhoneBook/PhoneDB.txt");
             InputStreamReader isr = new InputStreamReader(in, "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {

            String str;
            while ((str = br.readLine()) != null) {
                String[] personInfo = str.split(",");
                pList.add(new Person(personInfo[0], personInfo[1], personInfo[2]));
            }
        } catch (IOException e) {
            System.out.println("파일을 읽는 중에 오류가 발생했습니다: " + e.getMessage());
        }

        // 메뉴 번호를 받기 위한 스캐너 생성
        Scanner sc = new Scanner(System.in);

        boolean loop = true; // while문을 제어하기 위한 boolean 변수
        while (loop) {
            // 메뉴 화면 출력
            System.out.println("1.리스트 2.등록 3.삭제 4.검색 5.종료");
            System.out.println("-------------------------------------------------");
            System.out.print(">메뉴번호: ");

            // 메뉴번호 받기
            int menuNum;
            try {
                menuNum = sc.nextInt();
            } catch (Exception e) {
                System.out.println("[다시 입력해 주세요.]");
                sc.nextLine(); // 버퍼 비우기
                continue;
            }

            // 메뉴번호별 개별 코드 실행
            switch (menuNum) {
                case 1:
                    // 리스트 출력 코드
                    System.out.println("<1.리스트>");
                    for (int i = 0; i < pList.size(); i++) {
                        Person person = pList.get(i);
                        System.out.println((i + 1) + ".\t" + person.getName() + "\t" + person.getHp() + "\t" + person.getCompany());
                    }
                    break;

                case 2:
                    // 등록 코드
                    sc.nextLine(); // 이전의 nextInt() 호출로 남아 있는 개행 문자를 제거
                    System.out.print("이름: ");
                    String name = sc.nextLine();
                    System.out.print("휴대전화: ");
                    String hp = sc.nextLine();
                    System.out.print("회사전화: ");
                    String company = sc.nextLine();

                    // 등록
                    pList.add(new Person(name, hp, company));

                    // 파일 쓰기
                    try (FileOutputStream out = new FileOutputStream("/Users/yuchan/eclipse-workspace/Project_PhoneBook/PhoneDB.txt", true);
                         OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                         BufferedWriter bw = new BufferedWriter(osw)) {

                        bw.write(name + "," + hp + "," + company);
                        bw.newLine();
                    } catch (IOException e) {
                        System.out.println("파일을 쓰는 중에 오류가 발생했습니다: " + e.getMessage());
                    }
                    break;

                case 3:
                    // 삭제 코드
                    System.out.print(">번호 : ");
                    int delIndex = sc.nextInt() - 1;
                    if (delIndex >= 0 && delIndex < pList.size()) {
                        pList.remove(delIndex);
                        System.out.println("[삭제되었습니다.]");

                        // 파일 업데이트
                        try (FileOutputStream out = new FileOutputStream("/Users/yuchan/eclipse-workspace/Project_PhoneBook/PhoneDB.txt");
                             OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                             BufferedWriter bw = new BufferedWriter(osw)) {

                            for (Person p : pList) {
                                bw.write(p.getName() + "," + p.getHp() + "," + p.getCompany());
                                bw.newLine();
                            }
                        } catch (IOException e) {
                            System.out.println("파일을 업데이트 하는 중에 오류가 발생했습니다: " + e.getMessage());
                        }
                    } else {
                        System.out.println("잘못된 번호입니다.");
                    }
                    break;

                case 4:
                    // 검색 코드
                    sc.nextLine();
                    System.out.println("<4.검색>");
                    System.out.print(">이름: ");
                    String searchName = sc.nextLine();
                    for (int i = 0; i < pList.size(); i++) {
                        Person p = pList.get(i);
                        if (p.getName().contains(searchName)) {
                            System.out.println((i + 1) + ".\t" + p.getName() + "\t" + p.getHp() + "\t" + p.getCompany());
                        }
                    }
                    break;

                case 5:
                    // 종료 코드
                    System.out.println("프로그램을 종료합니다.");
                    System.out.println("*************************************************");
                    System.out.println("*\t\t    감사합니다.    \t\t*");
                    System.out.println("*************************************************");
                    loop = false;
                    break;

                default:
                    System.out.println("[다시 입력해 주세요.]");
                    break;
            }
        }

        sc.close(); // 스캐너 닫기
    }
}
