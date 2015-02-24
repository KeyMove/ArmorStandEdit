/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import static java.lang.System.out;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

/**
 *
 * @author Administrator
 */
public class ArmorStandEdit extends JavaPlugin implements Listener{

    Map<Player, ArmorStand> 玩家缓存=new HashMap<>();
    Inventory 扩展功能窗口;
    Inventory 动作窗口;
    class 镜头调节 extends Thread{
        Player 触发玩家;
        ArmorStand 盔甲架;
        Boolean 状态;

        public 镜头调节(Player 玩家,ArmorStand 盔甲) {
            触发玩家=玩家;
            盔甲架=盔甲;
        }
        public void 开始调节(){
            状态=true;
            this.start();
        }

        @Override
        public void run() {
            while(状态){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    
                }
            }
        }
    }
    
    public boolean 反射法修改私有成员变量(Object 类,String 名称,Object 值){
        try {
            Field f=类.getClass().getDeclaredField(名称);
            f.setAccessible(true);
            f.set(类, 值);
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            out.print(ex);
        }
        return false;
    }
    
    ItemStack 创建物品(Material 类型,String 名称,String 标记){
        ItemStack 物品=new ItemStack(类型);
        ItemMeta 物品属性=物品.getItemMeta();
        if(标记!=null){
            List<String> lore=new ArrayList<>();
            lore.add(标记);
            物品属性.setLore(lore);
        }
        if(名称!=null){
            物品属性.setDisplayName(名称);
        }
        物品.setItemMeta(物品属性);
        return 物品;
    }
    
    ItemStack 创建物品(Material 类型,int 后缀,String 名称,String 标记){
        ItemStack 物品=new ItemStack(类型, 1, (short) 后缀);
        ItemMeta 物品属性=物品.getItemMeta();
        if(标记!=null){
            List<String> lore=new ArrayList<>();
            lore.add(标记);
            物品属性.setLore(lore);
        }
        if(名称!=null){
            物品属性.setDisplayName(名称);
        }
        物品.setItemMeta(物品属性);
        return 物品;
    }
    
    Inventory 创建盔甲架编辑窗口(ArmorStand 盔甲架){
        Inventory 窗口=getServer().createInventory(null, 18, "§6§l编辑盔甲架");
        String 盔甲大小="大";
        String 盔甲隐藏="隐藏";
        String 盔甲重力="否";
        String 盔甲底座="隐藏";
        String 盔甲手臂="隐藏";
        if(盔甲架.isSmall()){
            盔甲大小="小";
        }
        if(盔甲架.isVisible()){
            盔甲隐藏="显示";
        }
        if(盔甲架.hasGravity()){
            盔甲重力="是";
        }
        if(盔甲架.hasBasePlate()){
            盔甲底座="显示";
        }
        if(盔甲架.hasArms()){
            盔甲手臂="显示";
        }
        窗口.setItem(0, 创建物品(Material.IRON_HELMET, "§r§6§l盔甲架头盔", "§r§a§n将头盔放到下面的格子"));
        窗口.setItem(1, 创建物品(Material.IRON_CHESTPLATE, "§r§6§l盔甲架盔甲", "§r§a§n将盔甲放到下面的格子"));
        窗口.setItem(2, 创建物品(Material.IRON_LEGGINGS, "§r§6§l盔甲架裤子", "§r§a§n将裤子放到下面的格子"));
        窗口.setItem(3, 创建物品(Material.IRON_BOOTS, "§r§6§l盔甲架鞋子", "§r§a§n将鞋子放到下面的格子"));
        窗口.setItem(4, 创建物品(Material.IRON_SWORD, "§r§6§l盔甲架武器", "§r§a§n将武器放到下面的格子"));
        窗口.setItem(5, 创建物品(Material.NAME_TAG, "§r§6§l盔甲架名称", "§r§a§n将头盔放到下面的格子"));
        
        窗口.setItem(6, 创建物品(Material.SLIME_BALL, "§r§6§l盔甲架大小","§r§3"+ 盔甲大小));
        窗口.setItem(7, 创建物品(Material.GLASS, "§r§6§l盔甲架是否隐藏", "§r§3"+盔甲隐藏));
        窗口.setItem(8, 创建物品(Material.SAND, "§r§6§l盔甲架是否受重力影响", "§r§3"+盔甲重力));
        窗口.setItem(15, 创建物品(Material.STEP, "§r§6§l盔甲架是否隐藏底座", "§r§3"+盔甲底座));
        窗口.setItem(16, 创建物品(Material.STICK, "§r§6§l盔甲架是否隐藏手臂", "§r§3"+盔甲手臂));
        窗口.setItem(17, 创建物品(Material.PAPER, "§r§6§l扩展功能", "§r其他扩展功能"));
        
        窗口.setItem(9, 盔甲架.getHelmet());
        窗口.setItem(10, 盔甲架.getChestplate());
        窗口.setItem(11, 盔甲架.getLeggings());
        窗口.setItem(12, 盔甲架.getBoots());
        窗口.setItem(13, 盔甲架.getItemInHand());
        if(盔甲架.isCustomNameVisible())
        {
            //out.print(盔甲架.getCustomName());
            窗口.setItem(14, 创建物品(Material.NAME_TAG, 盔甲架.getCustomName(),null));
        }
        
        return 窗口;
    }
    
    void 静态物品窗口(){
        扩展功能窗口=getServer().createInventory(null, 18, "§6§l盔甲架扩展");
        扩展功能窗口.setItem(0, 创建物品(Material.WOOL,5, "§r§6§l启用普通玩家对盔甲架操作", "§r§a启用后玩家可以替换盔甲架上的装备"));
        扩展功能窗口.setItem(1, 创建物品(Material.WOOL,14, "§r§6§l禁用普通玩家对盔甲架操作", "§r§a禁用后玩家不能替换盔甲架上的装备"));
        扩展功能窗口.setItem(2, 创建物品(Material.SKULL_ITEM,3, "§r§6§l设置面向玩家", "§r§a让盔甲架面向玩家"));
        扩展功能窗口.setItem(3, 创建物品(Material.ARMOR_STAND, "§r§6§l重置盔甲架动作", "§r§a让盔甲架变成放置时的姿势"));
        
    }
    
    ItemStack 重置物品标记(ItemStack 物品,String 标记){
        ItemMeta 物品属性=物品.getItemMeta();
        List<String> 字符串=物品属性.getLore();
        字符串.clear();
        字符串.add(标记);
        物品属性.setLore(字符串);
        物品.setItemMeta(物品属性);
        return 物品;
    }
    
    @EventHandler
    void 玩家右键(PlayerInteractAtEntityEvent 右键实体){
        if(右键实体.getRightClicked().getType()!=EntityType.ARMOR_STAND){
            return;
        }
        Player 玩家=右键实体.getPlayer();
        //if(!(玩家.hasPermission("op")||玩家.hasPermission("ArmorStandEdit")||(玩家.getGameMode()==GameMode.CREATIVE)))
        if(!(玩家.getGameMode()==GameMode.CREATIVE))
            return;
        ArmorStand 盔甲架=(ArmorStand)右键实体.getRightClicked();
        玩家.openInventory(创建盔甲架编辑窗口(盔甲架));
        玩家缓存.put(玩家, 盔甲架);
        
    }
    
    @EventHandler
    void 玩家编辑完成(InventoryCloseEvent 编辑窗口){
        String 标题=编辑窗口.getView().getTitle();
        if(标题==null)
            return;
        if(!标题.equalsIgnoreCase("§6§l编辑盔甲架"))
        {
            return;
        }
        Player 玩家=(Player) 编辑窗口.getPlayer();
        Inventory 窗口=编辑窗口.getView().getTopInventory();
        ArmorStand 盔甲架=玩家缓存.get(玩家);
        ItemStack 物品=窗口.getItem(14);
        盔甲架.setHelmet(窗口.getItem(9));
        盔甲架.setChestplate(窗口.getItem(10));
        盔甲架.setLeggings(窗口.getItem(11));
        盔甲架.setBoots(窗口.getItem(12));
        盔甲架.setItemInHand(窗口.getItem(13));
        
        if(!((物品!=null)&&(物品.getType()==Material.NAME_TAG))){
            //out.print("无物品退出");
            盔甲架.setCustomName("");
            盔甲架.setCustomNameVisible(false);
            return;
        }
        if(!(物品.hasItemMeta()&&物品.getItemMeta().hasDisplayName())){
            盔甲架.setCustomName("");
            盔甲架.setCustomNameVisible(false);
            return;
        }
        盔甲架.setCustomName(物品.getItemMeta().getDisplayName());
        盔甲架.setCustomNameVisible(true);
    }
    @EventHandler
    void 玩家操作物品(InventoryClickEvent 编辑窗口){
        String 窗口标题=编辑窗口.getView().getTitle();
        Player 玩家=(Player)编辑窗口.getWhoClicked();
        ArmorStand 盔甲架=玩家缓存.get(玩家);
        if(盔甲架==null||窗口标题==null)return;
        int id=编辑窗口.getRawSlot();
        Inventory 窗口=编辑窗口.getView().getTopInventory();
        switch(窗口标题){
            case "§6§l编辑盔甲架":
            if(id<18)
                if(id<9||id>14)
                    编辑窗口.setCancelled(true);
            switch(id){
                case 6:
                    if(盔甲架.isSmall())
                    {
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3大"));
                        盔甲架.setSmall(false);
                    }
                    else
                    {
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3小"));
                        盔甲架.setSmall(true);
                    }
                    break;
                case 7:
                    if(盔甲架.isVisible()){
                         窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3隐藏"));
                         盔甲架.setVisible(false);
                    }
                    else{
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3显示"));
                        盔甲架.setVisible(true);
                    }
                    break;
                case 8:
                    if(盔甲架.hasGravity()){
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3否"));
                        盔甲架.setGravity(false);
                    }else{
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3是"));
                        盔甲架.setGravity(true);
                    }
                    break;
                case 15:
                    if(盔甲架.hasBasePlate()){
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3隐藏"));
                        盔甲架.setBasePlate(false);
                    }else{
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3显示"));
                        盔甲架.setBasePlate(true);
                    }
                    break;
                case 16:
                    if(盔甲架.hasArms()){
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3隐藏"));
                        盔甲架.setArms(false);
                    }else{
                        窗口.setItem(id, 重置物品标记(窗口.getItem(id), "§r§3显示"));
                        盔甲架.setArms(true);
                    }
                    break;
                case 17:
                    玩家.closeInventory();
                    玩家.openInventory(扩展功能窗口);
                    break;
               
            }
            break;
            case "§6§l盔甲架扩展":
                if(id<18)编辑窗口.setCancelled(true);
                switch(id){
                    case 0:
                        反射法修改私有成员变量(((CraftArmorStand)盔甲架).getHandle(),"bg",0);//修改DisabledSlots值
                        break;
                    case 1:
                        反射法修改私有成员变量(((CraftArmorStand)盔甲架).getHandle(),"bg",31);//修改DisabledSlots值
                        break;
                    case 2:
                        Location 点=玩家.getLocation();
                        
                        点.setX(盔甲架.getLocation().getX());
                        点.setY(盔甲架.getLocation().getY());
                        点.setZ(盔甲架.getLocation().getZ());
                        点.setYaw(点.getYaw()-180);
                        盔甲架.teleport(点);
                        break;
                    case 3:
                        盔甲架.setHeadPose(EulerAngle.ZERO);
                        盔甲架.setLeftArmPose(EulerAngle.ZERO);
                        盔甲架.setRightArmPose(EulerAngle.ZERO);
                        盔甲架.setLeftLegPose(EulerAngle.ZERO);
                        盔甲架.setRightLegPose(EulerAngle.ZERO);
                        盔甲架.setBodyPose(EulerAngle.ZERO);
                        break;
                }
                break;
    }
    }
    
    @Override
    public void onEnable() {
        静态物品窗口();
        getServer().getPluginManager().registerEvents(this, this);
        out.print("盔甲架编辑器加载成功");
    }
    @Override
    public boolean onCommand(CommandSender 命令发送者, Command 命令, String label, String[] 参数列表) {
        if(!命令发送者.hasPermission("op")||!(命令发送者 instanceof Player)){
            return false;
        }
        Player 玩家=(Player)命令发送者;
        ArmorStand 盔甲架=玩家缓存.get(玩家);
        double val=0;
        if(盔甲架==null){
            玩家.sendMessage("§6[ArmorStandEdit] 未选中盔甲架!");
            return false;
        }
        if(参数列表.length<3){
            玩家.sendMessage("§6[ArmorStandEdit] §a------使用方法------");
            玩家.sendMessage("§6[ArmorStandEdit] §a第一个参数填0-5分别代表");
            玩家.sendMessage("§6[ArmorStandEdit] §a头(0) 左手(1) 右手(2) 身体(3) 左腿(4) 右腿(5)");
            玩家.sendMessage("§6[ArmorStandEdit] §a第二个参数填 X Y Z 坐标轴");
            玩家.sendMessage("§6[ArmorStandEdit] §a第三个参数填0-360角度");
            return false;
        }
        try {
            val=Double.parseDouble(参数列表[2])*Math.PI/180;
        } catch (NumberFormatException e) {
            玩家.sendMessage("§6[ArmorStandEdit] §4第三个参数只能为0-360角度!");
            return false;
        }
        switch(参数列表[0]){
            case "0":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setHeadPose(盔甲架.getHeadPose().setX(val));
                        break;
                    case "Y":
                        盔甲架.setHeadPose(盔甲架.getHeadPose().setY(val));
                        break;
                    case "Z":
                        盔甲架.setHeadPose(盔甲架.getHeadPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
                break;
            case "1":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setLeftArmPose(盔甲架.getLeftArmPose().setX(val));
                        break;
                    case "Y":
                        盔甲架.setLeftArmPose(盔甲架.getLeftArmPose().setY(val));
                        break;
                    case "Z":
                        盔甲架.setLeftArmPose(盔甲架.getLeftArmPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
                break;
            case "2":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setRightArmPose(盔甲架.getRightArmPose().setX(val));
                        break;
                    case "Y":
                        盔甲架.setRightArmPose(盔甲架.getRightArmPose().setY(val));
                        break;
                    case "Z":
                        盔甲架.setRightArmPose(盔甲架.getRightArmPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
                break;
            case "3":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setBodyPose(盔甲架.getBodyPose().setX(val));
                        break;
                    case "Y":
                         盔甲架.setBodyPose(盔甲架.getBodyPose().setY(val));
                        break;
                    case "Z":
                         盔甲架.setBodyPose(盔甲架.getBodyPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
                break;
            case "4":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setLeftLegPose(盔甲架.getLeftLegPose().setX(val));
                        break;
                    case "Y":
                        盔甲架.setLeftLegPose(盔甲架.getLeftLegPose().setY(val));
                        break;
                    case "Z":
                        盔甲架.setLeftLegPose(盔甲架.getLeftLegPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
               break;
            case "5":
                switch(参数列表[1]){
                    case "X":
                        盔甲架.setRightLegPose(盔甲架.getRightLegPose().setX(val));
                        break;
                    case "Y":
                        盔甲架.setRightLegPose(盔甲架.getRightLegPose().setY(val));
                        break;
                    case "Z":
                        盔甲架.setRightLegPose(盔甲架.getRightLegPose().setZ(val));
                        break;
                    default:
                        玩家.sendMessage("§6[ArmorStandEdit] §4第二个参数只能为X Y Z!");
                        return false;
                }
                break;
            default:
                玩家.sendMessage("§6[ArmorStandEdit] §4第一个参数只能为0-5的数字!");
                return false;
        }
        玩家.sendMessage("§6[ArmorStandEdit] §a设置成功！");
        
        return true;
    }
}
