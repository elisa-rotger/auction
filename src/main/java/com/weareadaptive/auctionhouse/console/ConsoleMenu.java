package com.weareadaptive.auctionhouse.console;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ConsoleMenu {

  public abstract void display(MenuContext context);

  protected MenuOption option(String title, Consumer<MenuContext> action) {
    return new MenuOption(title, action, c -> true);
  }

  protected MenuOption option(String title, Runnable action) {
    return new MenuOption(title, c -> action.run(), c -> true);
  }

  // isShown -> determine if the option should show
  protected MenuOption option(String title, Consumer<MenuContext> action,
                              Predicate<MenuContext> isShown) {
    return new MenuOption(title, action, isShown);
  }

  protected MenuOption option(String title, Runnable action, Predicate<MenuContext> isDisplayed) {
    return new MenuOption(title, c -> action.run(), isDisplayed);
  }

  protected MenuOption leave(String title) {
    return new MenuOption(title, c -> {
    }, c -> true, true);
  }

  protected void createMenu(MenuContext context, MenuOption... options) {
    var scanner = context.getScanner();
    var out = context.getOut();

    var finalOptions = Arrays.stream(options)
            .filter(option -> option.isDisplayed.test(context))
            .toArray(MenuOption[]::new);

    var success = false;
    while (!success) {
      out.println("===================================");
      for (var i = 1; i <= finalOptions.length; i++) {
        var option = finalOptions[i - 1];
        out.printf("%s. %s %n", i, option.title);
      }
      out.println("===================================");
      out.print("Enter your option: ");
      var invalidOption = false;
      try {
        var rawOption = scanner.nextLine();
        var option = Integer.parseInt(rawOption);
        if (option > 0 && option <= finalOptions.length) {
          finalOptions[option - 1].action.accept(context);
          success = finalOptions[option - 1].leave;
        } else {
          invalidOption = true;
        }
      } catch (InputMismatchException | NumberFormatException mismatchException) {
        invalidOption = true;
      }
      if (invalidOption) {
        out.print("Invalid option");
      }
    }
  }

  protected void pressEnter(MenuContext context) {
    context.getOut().println("Press Enter key to continue...");
    context.getScanner().nextLine();
  }

  protected String readPassword(Scanner scanner) {
    if (System.console() != null) {
      return new String(System.console().readPassword());
    } else {
      // There is no Console in IntelliJ
      return scanner.nextLine();
    }
  }

  public static class MenuOption {
    final String title;
    final Consumer<MenuContext> action;
    final boolean leave;
    private final Predicate<MenuContext> isDisplayed;

    private MenuOption(String title, Consumer<MenuContext> action,
                       Predicate<MenuContext> isDisplayed) {
      this(title, action, isDisplayed, false);
    }

    private MenuOption(String title, Consumer<MenuContext> action,
                       Predicate<MenuContext> isDisplayed, boolean leave) {
      this.isDisplayed = isDisplayed;
      this.leave = leave;
      this.title = title;
      this.action = action;
    }
  }

  public static <T> T[] append(T[] arr, T element) {
    final int N = arr.length;
    arr = Arrays.copyOf(arr, N + 1);
    arr[N] = element;
    return arr;
  }
}
